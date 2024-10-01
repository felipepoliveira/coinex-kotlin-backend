package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class SendPrimaryEmailChangeEmailDTO(
    /**
     * The new email
     */
    @field:Email
    @field:NotNull
    @field:NotEmpty
    val newEmail: String,
)