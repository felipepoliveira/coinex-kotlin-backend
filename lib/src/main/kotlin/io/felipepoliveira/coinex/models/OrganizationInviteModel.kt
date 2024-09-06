package org.example.io.felipepoliveira.coinex.models

import java.time.LocalDateTime
import java.util.*

class OrganizationInviteModel(
    /**
     * The id of the organization invite model
     */
    val id: Long,

    /**
     * The UUID of the organization invite model
     */
    val uuid: UUID,

    /**
     * The organization where the invites refer to
     */
    val organization: OrganizationModel,

    /**
     * The e-mail that will receive the invite
     */
    val recipientEmail: String,

    /**
     * Store the timestamp when the invite was created
     */
    val createdAt: LocalDateTime,
)