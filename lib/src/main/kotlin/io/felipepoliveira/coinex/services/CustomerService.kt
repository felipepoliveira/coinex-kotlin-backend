package io.felipepoliveira.coinex.services

import io.felipepoliveira.coinex.dao.CustomerDAO
import io.felipepoliveira.coinex.security.PasswordRank
import io.felipepoliveira.coinex.security.calculatePasswordRank
import io.felipepoliveira.coinex.security.hashPasswordToString
import io.felipepoliveira.coinex.services.dto.customer.CreateCustomerDTO
import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.models.CustomerModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.validation.SmartValidator
import java.time.LocalDate
import java.util.*

@Service
class CustomerService @Autowired constructor(
    val customerDAO: CustomerDAO,
    val userDAO: UserDAO,
    val userService: UserService,
    validator: SmartValidator,
) : BaseService(validator) {

    /**
     * Create a Customer account into the platform
     */
    fun registerCustomer(dto: CreateCustomerDTO): CustomerModel {

        // Validate the DTO
        val errors = validate(dto)
        if (errors.hasFieldErrors()) {
            throw BusinessRuleException(errors)
        }

        // Assert that the given password is at least Safe
        if (!calculatePasswordRank(dto.password).isAtLeast(PasswordRank.Safe)) {
            throw BusinessRuleException(
                BusinessRuleExceptionType.InvalidPassword,
                "The given password is considered unsafe to use"
            )
        }

        // Assert that the primary email is available to use
        if (userDAO.findByPrimaryEmail(dto.primaryEmail) != null) {
            throw BusinessRuleException(
                BusinessRuleExceptionType.InvalidEmail,
                "The email is ${dto.primaryEmail} is unavailable"
            )
        }

        // Persist the customer in the database
        val customer = CustomerModel(
            id = null,
            name = dto.name,
            uuid = UUID.randomUUID(),
            primaryEmail = dto.primaryEmail,
            hashedPassword = hashPasswordToString(dto.password),
            primaryEmailConfirmedAt = null,
            createdAt = LocalDate.now(),
            preferredLanguage = dto.preferredLanguage
        )
        customerDAO.persist(customer)

        // Send the email confirmation email to the customer when its account is created
        userService.sendEmailConfirmationEmail(customer)

        return customer
    }

}