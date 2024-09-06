package org.example.io.felipepoliveira.coinex.models

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class CurrencyTransactionLogModel(
    /**
     * Store the id of the currency transaction log
     */
    val id: Long,
    /**
     * Store the UUID of the currency transaction log
     */
    val uuid: UUID,
    /**
     * The user that made the transaction
     */
    val sender: UserModel,
    /**
     * The user that received the value in the transaction
     */
    val recipient: UserModel,
    /**
     * The amount transferred in the transaction
     */
    val amount: BigDecimal,
    /**
     * Date when the transaction was created
     */
    val createdAt: LocalDateTime
)