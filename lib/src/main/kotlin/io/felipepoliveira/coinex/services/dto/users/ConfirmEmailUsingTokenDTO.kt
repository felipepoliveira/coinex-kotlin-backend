package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.NotNull

data class ConfirmEmailUsingTokenDTO(
    /**
     * The token used to confirm the email
     */
    @field:NotNull
    val token: String,
)
