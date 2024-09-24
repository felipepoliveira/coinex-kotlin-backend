package io.felipepoliveira.coinex.dao.hibernate

import jakarta.persistence.EntityManager
import io.felipepoliveira.coinex.dao.UserDAO
import io.felipepoliveira.coinex.dao.hibernate.HibernateDAO
import io.felipepoliveira.coinex.dao.hibernate.fetchFirst
import io.felipepoliveira.coinex.models.UserModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class UserHibernate @Autowired constructor(
    entityManager: EntityManager
) : UserDAO, HibernateDAO<Long, UserModel>(entityManager) {

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

    override fun findByUuid(uuid: String): UserModel? {
        return prepareQuery("user")
            .where("user.uuid = :uuid")
            .createFetchQuery()
            .setParameter("uuid", uuid)
            .fetchFirst()
    }

    override fun getDAOModelType(): Class<UserModel> = UserModel::class.java

}