package org.example.io.felipepoliveira.coinex.models

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class CurrencyBalanceModel(
    /**
     * Represents the id of the currency balance model
     */
    val id: Long,
    /**
     * Store the UUID of the currency balance model
     */
    val uuid: UUID,
    /**
     * Store the user that owns the currency balance model
     */
    val owner: UserModel,
    /**
     * Store the currency model
     */
    val currency: CurrencyModel,

    /**
     * Store the balance of the currency
     */
    val balance: BigDecimal,

    /**
     * Timestamp where the last transaction was made
     */
    val lastTransactionAt: LocalDateTime,
)