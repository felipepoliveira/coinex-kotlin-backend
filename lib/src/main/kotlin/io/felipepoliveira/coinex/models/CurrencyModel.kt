package org.example.io.felipepoliveira.coinex.models

import java.util.UUID

class CurrencyModel(
    /**
     * Store the ID of the currency model
     */
    val id: Long,
    /**
     * Store the organization that owns the currency
     */
    val organization: OrganizationModel,
    /**
     * Store the UUID of the currency model
     */
    val uuid: UUID,
    /**
     * Store the name of the currency model
     */
    val name: String,
)