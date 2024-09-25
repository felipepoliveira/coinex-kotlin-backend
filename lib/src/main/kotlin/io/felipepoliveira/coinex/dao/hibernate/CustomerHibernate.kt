package io.felipepoliveira.coinex.dao.hibernate

import io.felipepoliveira.coinex.dao.CustomerDAO
import io.felipepoliveira.coinex.models.CustomerModel
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
open class CustomerHibernate(entityManager: EntityManager) : CustomerDAO, HibernateDAO<Long, CustomerModel>(entityManager) {

    override fun findById(id: Long): CustomerModel? {
        return prepareQuery("customer")
            .where("customer.id = :id")
            .createFetchQuery()
            .setParameter("id", id)
            .fetchFirst()
    }

    override fun getDAOModelType(): Class<CustomerModel> = CustomerModel::class.java
}