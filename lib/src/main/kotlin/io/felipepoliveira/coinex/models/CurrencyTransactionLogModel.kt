package io.felipepoliveira.coinex.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "currency_transaction_log", indexes = [
    Index(columnList = "uuid", name = "UI_uuid_AT_currency_transaction_log")
])
class CurrencyTransactionLogModel(
    /**
     * Store the id of the currency transaction log
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    /**
     * Store the UUID of the currency transaction log
     */
    @Column(name = "uuid", nullable = false)
    val uuid: UUID,
    /**
     * The user that made the transaction
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @NotNull
    val sender: UserModel,
    /**
     * The user that received the value in the transaction
     */
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    @NotNull
    val recipient: UserModel,
    /**
     * The amount transferred in the transaction
     */
    @Column(name = "amount", nullable = false, precision = 12, scale = 6)
    @NotNull
    val amount: BigDecimal,
    /**
     * Date when the transaction was created
     */
    @Column(name = "created_at", nullable = false)
    @NotNull
    val createdAt: LocalDateTime
)