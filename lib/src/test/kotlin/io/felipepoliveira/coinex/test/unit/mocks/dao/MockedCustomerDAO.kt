package io.felipepoliveira.coinex.test.unit.mocks.dao

import io.felipepoliveira.coinex.dao.CustomerDAO
import io.felipepoliveira.coinex.models.CustomerModel
import io.felipepoliveira.coinex.security.hashPasswordToString
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class MockedCustomerDAO : CustomerDAO, MockedDAO<Long, CustomerModel>() {

    private val mockedDatabase = mutableListOf<MockedObject<CustomerModel>>()

    init {
        mockedDatabase.add(MockedObject { customer1() })
    }

    fun customer1() = CustomerModel(
        name = "User 1",
        primaryEmail = "customer1@email.com",
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        createdAt = LocalDate.now(),
        hashedPassword = hashPasswordToString("Pwd123!c"),
        primaryEmailConfirmedAt = null,
        id = null,
    )

    override fun findById(id: Long): CustomerModel? {
        return mockedDatabase.find { m -> m.reference.id == id }?.mock()
    }

}