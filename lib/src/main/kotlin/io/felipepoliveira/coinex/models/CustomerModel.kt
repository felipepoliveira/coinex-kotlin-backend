package io.felipepoliveira.coinex.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "customer")
class CustomerModel(
    id: Long?,
    uuid: UUID,
    name: String,
    primaryEmail: String,
    hashedPassword: String,
    primaryEmailConfirmedAt: LocalDateTime?,
    createdAt: LocalDate,
    preferredLanguage: Language,
) : UserModel(id, uuid, name, primaryEmail, hashedPassword, primaryEmailConfirmedAt, createdAt, preferredLanguage)