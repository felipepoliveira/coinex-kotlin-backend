package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.NotNull

data class ChangePasswordUsingRecoveryTokenDTO(
    /**
     * The new password that will be used in the user account
     */
    @field:NotNull
    val password: String,

    /**
     * The reecovery token
     */
    @field:NotNull
    val token: String,
)