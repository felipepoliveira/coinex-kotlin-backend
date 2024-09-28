package io.felipepoliveira.coinex.dao.hibernate

import jakarta.persistence.EntityManager
import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.dao.hibernate.HibernateDAO
import io.felipepoliveira.coinex.dao.hibernate.fetchFirst
import io.felipepoliveira.coinex.models.UserModel
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
open class UserHibernate : UserDAO, HibernateDAO<Long, UserModel>() {

    override fun findById(id: Long): UserModel? {
        return prepareQuery("user")
            .where("user.id = :id")
            .createFetchQuery()
            .setParameter("id", id)
            .fetchFirst()
    }

    override fun findByPrimaryEmail(primaryEmail: String): UserModel? {
        return prepareQuery("user")
            .where("user.primaryEmail = :email")
            .createFetchQuery()
            .setParameter("email", primaryEmail)
            .fetchFirst()
    }

    override fun findByUuid(uuid: UUID): UserModel? {
        return prepareQuery("user")
            .where("user.uuid = :uuid")
            .createFetchQuery()
            .setParameter("uuid", uuid)
            .fetchFirst()
    }

    override fun getDAOModelType(): Class<UserModel> = UserModel::class.java

}