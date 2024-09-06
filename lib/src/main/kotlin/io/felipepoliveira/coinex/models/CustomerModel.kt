package org.example.io.felipepoliveira.coinex.models

import java.time.LocalDateTime
import java.util.*

class CustomerModel(
    id: Long,
    uuid: UUID,
    name: String,
    primaryEmail: String,
    hashedPassword: String,
    primaryEmailConfirmedAt: LocalDateTime

) : UserModel(id, uuid, name, primaryEmail, hashedPassword, primaryEmailConfirmedAt)