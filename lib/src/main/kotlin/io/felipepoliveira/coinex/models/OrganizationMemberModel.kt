package org.example.io.felipepoliveira.coinex.models

import java.time.LocalDateTime

class OrganizationMemberModel(
    /**
     * The id of the organization member model
     */
    val id: Long,

    /**
     * The user that is a member of the organization
     */
    val member: UserModel,

    /**
     * The organization where this member resides
     */
    val organization: OrganizationModel,

    /**
     * Store the date where the user becomes a member of the organization
     */
    val createdAt: LocalDateTime,

    /**
     * Store the granted roles of the organization member
     */
    val grantedRoles: Set<String>,
)