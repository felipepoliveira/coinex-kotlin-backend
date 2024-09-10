package org.example.io.felipepoliveira.coinex.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "customer")
class CustomerModel(
    id: Long,
    uuid: UUID,
    name: String,
    primaryEmail: String,
    hashedPassword: String,
    primaryEmailConfirmedAt: LocalDateTime

) : UserModel(id, uuid, name, primaryEmail, hashedPassword, primaryEmailConfirmedAt)