package io.felipepoliveira.coinex.dao

import io.felipepoliveira.coinex.models.UserModel
import java.util.UUID

interface UserDAO: DAO<Long, UserModel> {

    /**
     * Find a UserModel identified by its primary email
     */
    fun findByPrimaryEmail(primaryEmail: String): UserModel?

    /**
     * Find a UserModel identified by its uuid
     */
    fun findByUuid(uuid: UUID): UserModel?
}