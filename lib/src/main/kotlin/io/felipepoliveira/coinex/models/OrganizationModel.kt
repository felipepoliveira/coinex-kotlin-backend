package org.example.io.felipepoliveira.coinex.models

import java.util.UUID

class OrganizationModel(
    /**
     * Store the organization Id
     */
    val id: Long,

    /**
     * The user that owns the organization
     */
    val owner: UserModel,

    /**
     * Store the organization name
     */
    val name: String,

    /**
     * Store the organization unique code
     */
    val code: String,
)