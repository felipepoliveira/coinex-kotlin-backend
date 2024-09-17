package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class FindByEmailAndPasswordDTO(
    /**
     * The email of the user
     */
    @field:Email
    @field:NotNull
    val email: String,

    /**
     * The password of the user
     */
    @field:NotNull
    val password: String,
)