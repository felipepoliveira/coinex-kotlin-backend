package io.felipepoliveira.coinex.services

import com.auth0.jwt.exceptions.JWTVerificationException
import io.felipepoliveira.coinex.cache.ServiceTimeoutCache
import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.mail.UserMail
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.security.PasswordRank
import io.felipepoliveira.coinex.security.calculatePasswordRank
import io.felipepoliveira.coinex.security.hashPasswordToString
import io.felipepoliveira.coinex.security.tokens.EmailConfirmationTokenHandler
import io.felipepoliveira.coinex.security.tokens.PasswordRecoveryTokenHandler
import io.felipepoliveira.coinex.security.tokens.PrimaryEmailChangeTokenHandler
import io.felipepoliveira.coinex.security.tokens.PrimaryEmailChangeTokenPayload
import io.felipepoliveira.coinex.security.tokens.dto.EmailConfirmationTokenPayload
import io.felipepoliveira.coinex.security.tokens.dto.PasswordRecoveryTokenPayload
import io.felipepoliveira.coinex.security.verifyPassword
import io.felipepoliveira.coinex.services.dto.users.*
import io.felipepoliveira.coinex.utils.text.DIGITS
import io.felipepoliveira.coinex.utils.text.LETTERS_UPPER_CASE
import io.felipepoliveira.coinex.utils.text.generateRandomText
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.SmartValidator
import java.time.LocalDateTime
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class UserService @Autowired constructor(
    private val emailConfirmationTokenHandler: EmailConfirmationTokenHandler,
    private val passwordRecoveryTokenHandler: PasswordRecoveryTokenHandler,
    private val primaryEmailChangeTokenHandler: PrimaryEmailChangeTokenHandler,
    private val serviceTimeoutCache: ServiceTimeoutCache,
    private val userDAO: UserDAO,
    private val userMail: UserMail,
    validator: SmartValidator,
) : BaseService(validator) {

    companion object {
        val INVALID_CREDENTIALS_EXCEPTION: BusinessRuleException = BusinessRuleException(
            BusinessRuleExceptionType.NotFound,
            "Invalid credentials"
        )
    }

    /**
     * Return the user identified by the given UUID. If the user is not found the server will throw a exception
     * as this method expects that the user always exists
     */
    fun assertFindByUuid(uuid: UUID): UserModel {
        return userDAO.findByUuid(uuid) ?:
            throw Exception("Server expected to find the user with UUID $uuid but it is not on the database")
    }

    /**
     * Return the user identified by the given UUID. If the user is not found the server will throw a exception
     * as this method expects that the user always exists
     */
    fun assertFindByUuid(uuid: String): UserModel = assertFindByUuid(UUID.fromString(uuid))

    /**
     * Change the user password using the current password as authentication method
     */
    @Transactional
    fun changePasswordUsingCurrentPasswordAsAuthentication(
        serviceRequester: ServiceRequester,
        dto: ChangePasswordUsingCurrentPasswordAsAuthenticationDTO): UserModel {

        // check for validation errors
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // check for unsafe password
        if (!calculatePasswordRank(dto.newPassword).isAtLeast(PasswordRank.Safe)) {
            validationResult.addError("newPassword", "Password was considered unsafe by the server standards")
            throw BusinessRuleException(validationResult)
        }

        // if the current password is incorrect throw an exception
        val user = assertFindByUuid(serviceRequester.userUuid)
        if (!verifyPassword(user.hashedPassword, dto.currentPassword)) {
            throw BusinessRuleException(BusinessRuleExceptionType.InvalidCredentials, "Incorrect password")
        }

        // update the user password in the database
        user.hashedPassword = hashPasswordToString(dto.newPassword)
        userDAO.update(user)

        return user
    }

    /**
     * Change the password of the user using a recovery token method
     */
    fun changePasswordUsingRecoveryToken(dto: ChangePasswordUsingRecoveryTokenDTO): UserModel {
        // check for validation result on DTO
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // Calculate the password rank
        if (!calculatePasswordRank(dto.password).isAtLeast(PasswordRank.Safe)) {
            throw BusinessRuleException(
                BusinessRuleExceptionType.InvalidPassword,
                "Given password was not considered safe by the platform standards"
            )
        }

        // parse the token
        val decodedToken: PasswordRecoveryTokenPayload
        try {
            decodedToken = passwordRecoveryTokenHandler.validateAndParse(dto.token)
        } catch (e: JWTVerificationException) {
            throw BusinessRuleException(
                BusinessRuleExceptionType.InvalidCredentials,
                "Invalid or expired token provided"
            )
        }

        // Assert that user exists
        val user = userDAO.findByUuid(decodedToken.userUuid) ?:
            throw Exception("An fatal error occur on the server while trying to recover password: User with UUID ${decodedToken.userUuid} was not found in database")

        // Update the password
        user.hashedPassword = hashPasswordToString(dto.password)
        userDAO.update(user)

        return user
    }

    /**
     * Change the user primary email using token and secret code
     */
    fun changePrimaryEmailUsingTokenAndSecretCode(
        serviceRequester: ServiceRequester,
        dto: ChangePrimaryEmailUsingTokenAndSecretCodeDTO): UserModel {

        // make validation results
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // Validate and parse the token
        val tokenPayload: PrimaryEmailChangeTokenPayload
        try {
            tokenPayload = primaryEmailChangeTokenHandler.validateAndParse(dto.token, dto.code)
        } catch (e: JWTVerificationException) {
            throw BusinessRuleException(
                type = BusinessRuleExceptionType.InvalidCredentials,
                reason = "Email change token verification failed"
            )
        }

        // check for email availability
        if (userDAO.findByPrimaryEmail(tokenPayload.newEmail) != null) {
            throw BusinessRuleException(
                type = BusinessRuleExceptionType.InvalidEmail,
                reason = "The email '${tokenPayload.newEmail}' is not available"
            )
        }

        // Fetch the user from the database and update its primary email
        val requester = assertFindByUuid(serviceRequester.userUuid)
        requester.primaryEmail = tokenPayload.newEmail
        requester.primaryEmailConfirmedAt = LocalDateTime.now()
        userDAO.update(requester)

        return requester
    }

    /**
     * Confirm the user email using a token provided by EmailConfirmationTokenHandler
     */
    fun confirmEmailUsingToken(dto: ConfirmEmailUsingTokenDTO): UserModel {

        // check for validation results
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // Validate the token
        val tokenPayload: EmailConfirmationTokenPayload
        try {
            tokenPayload = emailConfirmationTokenHandler.validateAndParse(dto.token)
        } catch (e: JWTVerificationException) {
            throw BusinessRuleException(
                type = BusinessRuleExceptionType.InvalidCredentials,
                reason = "Invalid token provided"
            )
        }

        // Fetch the user using the uuid in token
        val user = userDAO.findByUuid(tokenPayload.userUuid) ?:
            throw Exception("Fatal error. Server expected to find user identified by UUID ${tokenPayload.userUuid} but it was not found")

        // Confirm the email
        user.primaryEmailConfirmedAt = LocalDateTime.now()
        userDAO.update(user)

        return user
    }

    /**
     * Find a user identified by its email and password
     */
    fun findByPrimaryEmailAndPassword(dto: FindByEmailAndPasswordDTO): UserModel {
        // validate the dto
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // check if the email and password are correct
        val user = userDAO.findByPrimaryEmail(dto.email) ?: throw INVALID_CREDENTIALS_EXCEPTION
        if (!verifyPassword(user.hashedPassword, dto.password)) {
            throw INVALID_CREDENTIALS_EXCEPTION
        }

        return user
    }

    /**
     * Send the e-mail confirmation mail to the given user
     */
    fun sendEmailConfirmationEmail(serviceRequester: ServiceRequester) {
        sendEmailConfirmationEmail(assertFindByUuid(serviceRequester.userUuid))
    }

    /**
     * Send the e-mail confirmation mail to the given user
     */
    fun sendEmailConfirmationEmail(user: UserModel) {
        serviceTimeoutCache.executeOnTimeout("USER-${user.uuid}-EMAIL_CONFIRM_MAIL", 5.toDuration(DurationUnit.MINUTES)) {
            val expiresAt = LocalDateTime.now().plusHours(1)
            val tokenPayload = emailConfirmationTokenHandler.issue(user, expiresAt)
            userMail.sendPasswordRecoveryMail(user, tokenPayload.token)
        }
    }

    /**
     * Send a password recovery email to the user that owns the given e-mail. The service will be included in a
     * timeout so that the e-mail can not be spammed
     */
    fun sendPasswordRecoveryEmail(email: String) {
        // if the user is not found return
        val user = userDAO.findByPrimaryEmail(email) ?: return

        serviceTimeoutCache.executeOnTimeout("USER-${user.uuid}-PWD_RCV_MAIL", 5.toDuration(DurationUnit.MINUTES)) {
            val expiresAt = LocalDateTime.now().plusDays(30)
            val tokenPayload = passwordRecoveryTokenHandler.issue(user, expiresAt)
            userMail.sendPasswordRecoveryMail(user, tokenPayload.token)
        }
    }

    fun sendPrimaryEmailChangeEmail(serviceRequester: ServiceRequester, dto: SendPrimaryEmailChangeEmailDTO) {
        // check for binding result errors
        val validationResult = validate(dto)
        if (validationResult.hasErrors()) {
            throw BusinessRuleException(validationResult)
        }

        // check for email availability
        if (userDAO.findByPrimaryEmail(dto.newEmail) != null) {
            throw BusinessRuleException(
                type = BusinessRuleExceptionType.InvalidEmail,
                reason = "The email '${dto.newEmail}' is not available"
            )
        }

        // Fetch the user to execute the service
        val requester = assertFindByUuid(serviceRequester.userUuid)

        serviceTimeoutCache.executeOnTimeout("USER-${serviceRequester.userUuid}-PRIMARY_EMAIL_CHANGE_MAIL", 5.toDuration(DurationUnit.MINUTES)) {
            val secretCode = generateRandomText(LETTERS_UPPER_CASE+DIGITS, 8)
            val tokenPayload = primaryEmailChangeTokenHandler.issue(
                user = requester,
                newEmail = dto.newEmail,
                expiresAt = LocalDateTime.now().plusHours(1),
                secretCode = secretCode
            )
            userMail.sendChangePrimaryEmailMail(requester, dto.newEmail, tokenPayload.token, secretCode)
        }
    }
}