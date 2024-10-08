package io.felipepoliveira.coinex.test.unit.mocks.dao

import io.felipepoliveira.coinex.dao.CustomerDAO
import io.felipepoliveira.coinex.models.CustomerModel
import io.felipepoliveira.coinex.models.Language
import io.felipepoliveira.coinex.security.hashPasswordToString
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class MockedCustomerDAO : CustomerDAO, MockedDAO<Long, CustomerModel>() {

    private val mockedDatabase = mutableListOf<MockedObject<CustomerModel>>()

    init {
        mockedDatabase.add(MockedObject { customer1() })
        mockedDatabase.add(MockedObject { customer2() })
    }

    fun customer1() = CustomerModel(
        name = "User 1",
        primaryEmail = "customer1@email.com",
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        createdAt = LocalDate.now(),
        hashedPassword = hashPasswordToString(MockedUserDAO.DEFAULT_PASSWORD),
        primaryEmailConfirmedAt = null,
        id = 1,
        preferredLanguage = Language.EN_US
    )

    fun customer2() = CustomerModel(
        name = "User 2",
        primaryEmail = "customer2@email.com",
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
        createdAt = LocalDate.now(),
        hashedPassword = hashPasswordToString(MockedUserDAO.DEFAULT_PASSWORD),
        primaryEmailConfirmedAt = null,
        id = 2,
        preferredLanguage = Language.EN_US
    )

    override fun findById(id: Long): CustomerModel? {
        return mockedDatabase.find { m -> m.reference.id == id }?.mock()
    }

}