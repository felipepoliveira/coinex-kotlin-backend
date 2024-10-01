package io.felipepoliveira.coinex.platformapi.rest.v1.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class SendPasswordRecoveryEmailDTO(
    /**
     * The email used in the recovery operation
     */
    @field:Email
    @field:NotNull
    val email: String
)
