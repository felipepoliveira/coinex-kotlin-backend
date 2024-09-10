package org.example.io.felipepoliveira.coinex.dao

import org.example.io.felipepoliveira.coinex.models.UserModel

interface UserDAO: DAO<Long, UserModel> {

    /**
     * Find a UserModel identified by its primary email
     */
    fun findByPrimaryEmail(primaryEmail: String): UserModel?
}