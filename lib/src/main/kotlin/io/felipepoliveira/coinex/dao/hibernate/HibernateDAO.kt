package io.felipepoliveira.coinex.dao.hibernate

import io.felipepoliveira.coinex.dao.DAO
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Query
import org.springframework.beans.factory.annotation.Autowired

abstract class HibernateDAO<K, T> : DAO<K, T> {

    /**
     * Store the entity manager from persistence context
     */
    @PersistenceContext
    lateinit var entityManager: EntityManager

    /**
     * Return an instance associated with the type that should be persisted in the database
     */
    abstract fun getDAOModelType(): Class<T>

    fun getDAOModelTypeIdentifier(): String = getDAOModelType().name

    override fun delete(obj: T) {
        entityManager.remove(obj)
    }

    override fun persist(obj: T): T {
        entityManager.persist(obj)
        return obj
    }

    /**
     * Create an instance of a HqlQueryBuilder that helps to build and execute query in a semantic-based logic
     */
    fun prepareQuery(entityAlias: String) = HqlQueryBuilder(this, entityAlias)

    override fun update(obj: T): T {
        entityManager.merge(obj)
        return obj
    }
}

class HqlQueryBuilder<K, T>(
    private val hibernateDAO: HibernateDAO<K, T>,
    private val entityAlias: String,
    private var whereClause: String? = null
) {

    /**
     * Create a query with a command that will fetch data from the database
     */
    fun createFetchQuery(): Query = hibernateDAO.entityManager.createQuery(fetchHql())

    private fun fetchHql(): String {
        var query = "SELECT $entityAlias FROM ${hibernateDAO.getDAOModelTypeIdentifier()} $entityAlias"

        // include where clausule
        if (whereClause != null) {
            query += " $whereClause"
        }

        return query
    }

    fun where(criteria: String) : HqlQueryBuilder<K, T> {
        this.whereClause = "WHERE $criteria"
        return this
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Query.fetchAll(): List<T> {
    return this.resultList as List<T>
}

fun <T> Query.fetchFirst(): T? {
    var result = this.fetchAll<T>()
    if (result.isEmpty()) {
        return null
    }
    return result[0]
}