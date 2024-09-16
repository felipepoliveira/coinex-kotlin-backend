package io.felipepoliveira.coinex.dao

/**
 * Main abstraction for every data access object entity
 */
interface DAO<K, T> {

    /**
     * Delete an entity from the database
     */
    fun delete(obj: T)

    /**
     * Find an entity identified by its id
     */
    fun findById(id: K): T?

    /**
     * Persist the object in the database
     */
    fun persist(obj: T): T

    /**
     * Update the object in the database
     */
    fun update(obj: T): T
}