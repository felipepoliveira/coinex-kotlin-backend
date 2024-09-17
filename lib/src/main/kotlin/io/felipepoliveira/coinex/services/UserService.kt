package io.felipepoliveira.coinex.services

import io.felipepoliveira.coinex.cache.ServiceTimeoutCache
import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.mail.UserMail
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.security.tokens.PasswordRecoveryTokenHandler
import io.felipepoliveira.coinex.security.verifyPassword
import io.felipepoliveira.coinex.services.dto.users.FindByEmailAndPasswordDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.validation.SmartValidator
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class UserService @Autowired constructor(
    private val passwordRecoveryTokenHandler: PasswordRecoveryTokenHandler,
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

    fun sendPasswordRecoveryEmail(email: String) {
        // if the user is not found return
        val user = userDAO.findByPrimaryEmail(email) ?: return

        serviceTimeoutCache.executeOnTimeout("USER-${user.uuid}-PWD_RCV_MAIL", 5.toDuration(DurationUnit.MINUTES)) {
            val expiresAt = LocalDateTime.now().plusDays(30)
            val tokenPayload = passwordRecoveryTokenHandler.issue(user, expiresAt)
            userMail.sendPasswordRecoveryMail(user, tokenPayload.token)
        }
    }
}