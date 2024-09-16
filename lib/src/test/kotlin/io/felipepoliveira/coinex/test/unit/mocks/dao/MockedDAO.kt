package io.felipepoliveira.coinex.test.unit.mocks.dao

import io.felipepoliveira.coinex.dao.DAO

abstract class MockedDAO<K, T> : DAO<K, T> {
    override fun delete(obj: T) {
        println("Mocked delete() called from DAO")
    }

    override fun persist(obj: T): T {
        println("Mocked persist() called from DAO")
        return obj
    }

    override fun update(obj: T): T {
        println("Mocked update() called from DAO")
        return obj
    }

}