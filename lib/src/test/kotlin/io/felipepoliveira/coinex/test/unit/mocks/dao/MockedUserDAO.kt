package io.felipepoliveira.coinex.test.unit.mocks.dao

import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.security.hashPasswordToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class MockedUserDAO @Autowired constructor(
    val mockedCustomerDAO: MockedCustomerDAO,
) : UserDAO, MockedDAO<Long, UserModel>() {

    private val mockedDatabase = mutableListOf<MockedObject<UserModel>>()

    init {
        mockedDatabase.add(MockedObject { user1() })
    }

    fun user1() = mockedCustomerDAO.customer1()

    override fun findByPrimaryEmail(primaryEmail: String): UserModel? {
        return mockedDatabase.find { m -> m.reference.primaryEmail == primaryEmail }?.mock()
    }

    override fun findById(id: Long): UserModel? {
        return mockedDatabase.find { m -> m.reference.id == id }?.mock()
    }
}