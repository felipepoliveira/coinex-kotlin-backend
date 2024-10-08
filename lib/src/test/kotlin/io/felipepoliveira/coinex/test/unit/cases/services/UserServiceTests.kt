package io.felipepoliveira.coinex.test.unit.cases.services

import io.felipepoliveira.coinex.ext.asServiceRequester
import io.felipepoliveira.coinex.security.tokens.PasswordRecoveryTokenHandler
import io.felipepoliveira.coinex.security.tokens.PrimaryEmailChangeTokenHandler
import io.felipepoliveira.coinex.security.verifyPassword
import io.felipepoliveira.coinex.services.BusinessRuleException
import io.felipepoliveira.coinex.services.BusinessRuleExceptionType
import io.felipepoliveira.coinex.services.UserService
import io.felipepoliveira.coinex.services.dto.users.*
import io.felipepoliveira.coinex.test.UnitTestsConfigurations
import io.felipepoliveira.coinex.test.unit.mocks.dao.MockedUserDAO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class ChangePasswordUsingCurrentPasswordAsAuthentication @Autowired constructor(
    private val mockedUserDAO: MockedUserDAO,
    private val userService: UserService,
) : FunSpec({
    test("Test success") {
        // Arrange
        val requester = mockedUserDAO.user1().asServiceRequester()
        val dto = ChangePasswordUsingCurrentPasswordAsAuthenticationDTO(
            currentPassword = MockedUserDAO.DEFAULT_PASSWORD,
            newPassword = "NewPwd!132@"
        )

        // Act
        val user = userService.changePasswordUsingCurrentPasswordAsAuthentication(requester, dto)

        // Assert
        verifyPassword(user.hashedPassword, dto.newPassword) shouldBe true
    }

    test("Test if fails when new password is considered unsafe") {
        // Arrange
        val requester = mockedUserDAO.user1().asServiceRequester()
        val dto = ChangePasswordUsingCurrentPasswordAsAuthenticationDTO(
            currentPassword = MockedUserDAO.DEFAULT_PASSWORD,
            newPassword = "unsafepwd"
        )

        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePasswordUsingCurrentPasswordAsAuthentication(requester, dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.DataValidation
        exception.errorDetails?.containsKey("newPassword") shouldBe true
    }

    test("Test if fails when current password is wrong") {
        // Arrange
        val requester = mockedUserDAO.user1().asServiceRequester()
        val dto = ChangePasswordUsingCurrentPasswordAsAuthenticationDTO(
            currentPassword = "wrong_password",
            newPassword = "NewPwd!132@"
        )

        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePasswordUsingCurrentPasswordAsAuthentication(requester, dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.InvalidCredentials
    }
})

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class ChangePasswordUsingRecoveryTokenTests @Autowired constructor(
    private val mockedUserDAO: MockedUserDAO,
    private val passwordRecoveryTokenHandler: PasswordRecoveryTokenHandler,
    private val userService: UserService,
) : FunSpec({
    test("Test success") {
        // Arrange
        val targetUser = mockedUserDAO.user1()
        val tokenPayload = passwordRecoveryTokenHandler.issue(targetUser, expiresAt = LocalDateTime.now().plusDays(30))
        val dto = ChangePasswordUsingRecoveryTokenDTO(
            password = "Pwd!132@cc",
            token = tokenPayload.token,
        )
        // Act
        val updatedUser = userService.changePasswordUsingRecoveryToken(dto)

        // Assert
        verifyPassword(updatedUser.hashedPassword, dto.password) shouldBe true
    }

    test("Test if fails when token is invalid") {
        // Arrange
        val targetUser = mockedUserDAO.user1()
        val dto = ChangePasswordUsingRecoveryTokenDTO(
            password = "Pwd!132@cc",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        )
        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePasswordUsingRecoveryToken(dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.InvalidCredentials
    }

    test("Test if fails when password is considered unsafe") {
        // Arrange
        val targetUser = mockedUserDAO.user1()
        val tokenPayload = passwordRecoveryTokenHandler.issue(targetUser, expiresAt = LocalDateTime.now().plusDays(30))
        val dto = ChangePasswordUsingRecoveryTokenDTO(
            password = "unsafepwd",
            token = tokenPayload.token,
        )
        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePasswordUsingRecoveryToken(dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.InvalidPassword
    }
})

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class ChangePrimaryEmailUsing @Autowired constructor(
    private val mockedUserDAO: MockedUserDAO,
    private val primaryEmailChangeTokenHandler: PrimaryEmailChangeTokenHandler,
    private val userService: UserService,
) : FunSpec({

    test("Test success") {
        // Arrange
        val requester = mockedUserDAO.user1()
        val newEmail = "new@email.com"
        val secretCode = "ABC123"
        val dto = ChangePrimaryEmailUsingTokenAndSecretCodeDTO(
            token = primaryEmailChangeTokenHandler.issue(requester, newEmail, secretCode, expiresAt = LocalDateTime.now().plusHours(1)).token,
            code = secretCode
        )
        // Act
        val updatedUser = userService.changePrimaryEmailUsingTokenAndSecretCode(requester.asServiceRequester(), dto)

        // Assert
        updatedUser.primaryEmail shouldBe newEmail
    }

    test("Test if fails when secret code is incorrect") {
        // Arrange
        val requester = mockedUserDAO.user1()
        val newEmail = "new@email.com"
        val secretCode = "ABC123"
        val dto = ChangePrimaryEmailUsingTokenAndSecretCodeDTO(
            token = primaryEmailChangeTokenHandler.issue(requester, newEmail, secretCode, expiresAt = LocalDateTime.now().plusHours(1)).token,
            code = "INVALID_CODE"
        )
        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePrimaryEmailUsingTokenAndSecretCode(requester.asServiceRequester(), dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.InvalidCredentials
    }

    test("Test if fails when the newEmail in token is not available anymore because another user has the email") {
        // Arrange
        val requester = mockedUserDAO.user1()
        val newEmail = mockedUserDAO.user2().primaryEmail
        val secretCode = "ABC123"
        val dto = ChangePrimaryEmailUsingTokenAndSecretCodeDTO(
            token = primaryEmailChangeTokenHandler.issue(requester, newEmail, secretCode, expiresAt = LocalDateTime.now().plusHours(1)).token,
            code = secretCode
        )
        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.changePrimaryEmailUsingTokenAndSecretCode(requester.asServiceRequester(), dto) }

        // Assert
        exception.type shouldBe BusinessRuleExceptionType.InvalidEmail
    }
})

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class FindByEmailAndPasswordTests @Autowired constructor(
    private val mockedUserDAO: MockedUserDAO,
    private val userService: UserService,
) : FunSpec({
    test("Success when email and password are correct") {
        val referenceUser = mockedUserDAO.user1()
        val dto = FindByEmailAndPasswordDTO(
            email = referenceUser.primaryEmail,
            password = MockedUserDAO.DEFAULT_PASSWORD
        )

        val returnedUser = userService.findByPrimaryEmailAndPassword(dto)

        returnedUser.uuid shouldBe referenceUser.uuid
        returnedUser.id shouldBe referenceUser.id
        returnedUser.name shouldBe referenceUser.name
        returnedUser.primaryEmail shouldBe referenceUser.primaryEmail
    }

    test("Test if fails when email is a invalid email") {
        val dto = FindByEmailAndPasswordDTO(
            email = "invalid_email",
            password = MockedUserDAO.DEFAULT_PASSWORD
        )

        val exception = shouldThrow<BusinessRuleException> { userService.findByPrimaryEmailAndPassword(dto) }

        exception.type shouldBe BusinessRuleExceptionType.DataValidation
        exception.errorDetails?.containsKey("email") shouldBe true
    }

    test("Test if fails when email is incorrect") {
        val dto = FindByEmailAndPasswordDTO(
            email = "invalid@email.com",
            password = MockedUserDAO.DEFAULT_PASSWORD
        )

        val exception = shouldThrow<BusinessRuleException> { userService.findByPrimaryEmailAndPassword(dto) }

        exception.type shouldBe BusinessRuleExceptionType.NotFound
    }

    test("Test if fails when password is invalid") {
        val referenceUser = mockedUserDAO.user1()
        val dto = FindByEmailAndPasswordDTO(
            email = referenceUser.primaryEmail,
            password = "invalid password"
        )

        val exception = shouldThrow<BusinessRuleException> { userService.findByPrimaryEmailAndPassword(dto) }

        exception.type shouldBe BusinessRuleExceptionType.NotFound
    }
})

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class SendPrimaryEmailChangeEmail @Autowired constructor(
    private val mockedUserDAO: MockedUserDAO,
    private val userService: UserService,
) : FunSpec({

    test("Test success") {
        // Arrange
        val requester = mockedUserDAO.user1()
        val dto = SendPrimaryEmailChangeEmailDTO(
            newEmail = "new@email.com"
        )

        // Act
        userService.sendPrimaryEmailChangeEmail(requester.asServiceRequester(),dto)
    }

    test("Test if fails when DTO has invalid data"){
        // Arrange
        val requester = mockedUserDAO.user1()
        val dto = SendPrimaryEmailChangeEmailDTO(
            newEmail = ""
        )

        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.sendPrimaryEmailChangeEmail(requester.asServiceRequester(),dto) }

        // Arrange
        exception.type shouldBe BusinessRuleExceptionType.DataValidation
        exception.errorDetails?.containsKey("newEmail") shouldBe true
    }

    test("Test if fails when the target new Email its owned by another user") {
        // Arrange
        val requester = mockedUserDAO.user1()
        val dto = SendPrimaryEmailChangeEmailDTO(
            newEmail = mockedUserDAO.user2().primaryEmail
        )

        // Act
        val exception = shouldThrow<BusinessRuleException> { userService.sendPrimaryEmailChangeEmail(requester.asServiceRequester(),dto) }

        // Arrange
        exception.type shouldBe BusinessRuleExceptionType.InvalidEmail
    }
})