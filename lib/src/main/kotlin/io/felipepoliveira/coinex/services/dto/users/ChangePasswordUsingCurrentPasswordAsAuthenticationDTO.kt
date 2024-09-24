package io.felipepoliveira.coinex.services.dto.users

import jakarta.validation.constraints.NotNull

data class ChangePasswordUsingCurrentPasswordAsAuthenticationDTO(
    /**
     * The current password of the user
     */
    @field:NotNull
    val currentPassword: String,

    /**
     * The new password that will be applied into the user account
     */
    @field:NotNull
    val newPassword: String,
)