package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.NotNull

data class ChangePrimaryEmailUsingTokenAndSecretCodeDTO(
    /**
     * The secret code used in the email change process
     */
    @field:NotNull
    val code: String,
    /**
     * The token used to change the primary email
     */
    @field:NotNull
    val token: String,
)