package io.felipepoliveira.coinex.test.unit.cases.services

import io.felipepoliveira.coinex.services.BusinessRuleException
import io.felipepoliveira.coinex.services.BusinessRuleExceptionType
import io.felipepoliveira.coinex.services.UserService
import io.felipepoliveira.coinex.services.dto.users.FindByEmailAndPasswordDTO
import io.felipepoliveira.coinex.test.UnitTestsConfigurations
import io.felipepoliveira.coinex.test.unit.mocks.dao.MockedUserDAO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

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