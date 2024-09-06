package org.example.io.felipepoliveira.coinex.models

import java.time.LocalDateTime
import java.util.UUID

open class UserModel(
    /**
     * Id of the user represented by UNSIGNED INT64
     */
    val id: Long,
    /**
     * Generated UUID for the user model
     */
    val uuid: UUID,
    /**
     * The name of the user
     */
    val name: String,
    /**
     * The primary e-mail of the user
     */
    val primaryEmail: String,
    /**
     * The hashed password of the user
     */
    val hashedPassword: String,
    /**
     * Timestamp when the primary email was confirmed
     */
    val primaryEmailConfirmedAt: LocalDateTime,
)