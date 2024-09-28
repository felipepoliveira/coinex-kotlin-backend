package io.felipepoliveira.coinex.security.tokens.dto

import java.time.LocalDateTime
import java.util.UUID

data class PasswordRecoveryTokenPayload(
    /**
     * The UUID of the user that owns the token
     */
    val userUuid: UUID,

    /**
     * When the token was issued
     */
    val issuedAt: LocalDateTime,

    /**
     * When the token will expires
     */
    val expiresAt: LocalDateTime,

    /**
     * The generated token
     */
    val token: String,
)