package io.felipepoliveira.coinex.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user", indexes = [
    Index(columnList = "email", name = "UI_email_AT_user"),
    Index(columnList = "uuid", name = "UI_uuid_AT_user"),
])
open class UserModel(
    /**
     * ID of the user represented by UNSIGNED INT64
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    /**
     * Generated UUID for the user model
     */
    @Column(name = "uuid", nullable = false)
    val uuid: UUID,
    /**
     * The name of the user
     */
    @Column(name = "name", nullable = false, length = 60)
    @field:Size(min = 2, max = 60)
    @field:NotNull
    val name: String,
    /**
     * The primary e-mail of the user
     */
    @Column(name = "primary_email", nullable = false, length = 120)
    @field:Email
    @field:NotNull
    val primaryEmail: String,
    /**
     * The hashed password of the user
     */
    @Column(name = "hsd_pwd", nullable = false, length = 64)
    @NotNull
    val hashedPassword: String,
    /**
     * Timestamp when the primary email was confirmed
     */
    @Column(name = "primary_email_confirmed_at", nullable = true)
    val primaryEmailConfirmedAt: LocalDateTime?,

    /**
     * Date when the account was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @field:NotNull
    val createdAt: LocalDate,
)