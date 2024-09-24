package io.felipepoliveira.coinex.services

import io.felipepoliveira.coinex.models.UserModel
import java.util.UUID

/**
 * Class used to authenticate a service requester
 */
data class ServiceRequester(
    /**
     * The user UUID of the service requester
     */
    val userUuid: UUID,
) {
    companion object {
        /**
         * Create a ServiceRequester instance based on the data provided in the user class
         */
        fun fromUser(user: UserModel) = ServiceRequester(user.uuid)
    }
}