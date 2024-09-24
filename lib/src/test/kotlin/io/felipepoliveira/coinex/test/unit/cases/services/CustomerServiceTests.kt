package io.felipepoliveira.coinex.test.unit.cases.services

import io.felipepoliveira.coinex.models.Language
import io.felipepoliveira.coinex.security.verifyPassword
import io.felipepoliveira.coinex.services.BusinessRuleException
import io.felipepoliveira.coinex.services.BusinessRuleExceptionType
import io.felipepoliveira.coinex.services.CustomerService
import io.felipepoliveira.coinex.services.dto.customer.CreateCustomerDTO
import io.felipepoliveira.coinex.test.UnitTestsConfigurations
import io.felipepoliveira.coinex.test.unit.mocks.dao.MockedCustomerDAO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [UnitTestsConfigurations::class])
class RegisterCustomerTests @Autowired constructor(
    private val customerService: CustomerService,
    private val mockedCustomerDAO: MockedCustomerDAO,
) : FunSpec({

    test("Success when using valid DTO") {
        val dto = CreateCustomerDTO(
            name = "Test Customer",
            primaryEmail = "customer@email.com",
            password = "Pwd132!c",
            preferredLanguage = Language.EN_US,
        )

        val createdCustomer = customerService.registerCustomer(dto)

        createdCustomer.shouldNotBeNull()
        createdCustomer.createdAt.shouldNotBeNull()
        createdCustomer.name shouldBe dto.name
        verifyPassword(createdCustomer.hashedPassword, dto.password) shouldBe true
        createdCustomer.primaryEmail shouldBe dto.primaryEmail
        createdCustomer.primaryEmailConfirmedAt.shouldBeNull()
        createdCustomer.uuid.shouldNotBeNull()
    }

    test("Fails when dto is invalid") {
        val dto = CreateCustomerDTO(
            name = "",
            primaryEmail = "not a email",
            password = "1",
            preferredLanguage = Language.EN_US,
        )

        val exception = shouldThrow<BusinessRuleException> { customerService.registerCustomer(dto) }

        exception.type shouldBe BusinessRuleExceptionType.DataValidation
        exception.errorDetails?.containsKey("name") shouldBe true
        exception.errorDetails?.containsKey("primaryEmail") shouldBe true
        exception.errorDetails?.containsKey("password") shouldBe true
    }

    test("Fails when password is weak") {
        val dto = CreateCustomerDTO(
            name = "Test Customer",
            primaryEmail = "customer@email.com",
            password = "weakpassword",
            preferredLanguage = Language.EN_US,
        )

        val exception = shouldThrow<BusinessRuleException> { customerService.registerCustomer(dto) }

        exception.type shouldBe BusinessRuleExceptionType.InvalidPassword
    }

    test("Fails when email is duplicated") {
        val dto = CreateCustomerDTO(
            name = "Test Customer",
            primaryEmail = mockedCustomerDAO.customer1().primaryEmail,
            password = "Pwd132!c",
            preferredLanguage = Language.EN_US,
        )

        val exception = shouldThrow<BusinessRuleException> { customerService.registerCustomer(dto) }

        exception.type shouldBe BusinessRuleExceptionType.InvalidEmail
    }
})