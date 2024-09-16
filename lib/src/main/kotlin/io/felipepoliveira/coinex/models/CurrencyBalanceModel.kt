package io.felipepoliveira.coinex.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import io.felipepoliveira.coinex.models.CurrencyModel
import io.felipepoliveira.coinex.models.UserModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "currency_balance", indexes = [
    Index(columnList = "currency_id, name_id", name = "UI_currency_id_AND_name_id_AT_currency_balance"),
    Index(columnList = "uuid", name = "UI_uuid_AT_currency_balance")
])
class CurrencyBalanceModel(
    /**
     * Represents the id of the currency balance model
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    /**
     * Store the UUID of the currency balance model
     */
    @Column(name = "uuid", nullable = false)
    @NotNull
    val uuid: UUID,
    /**
     * Store the user that owns the currency balance model
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    val owner: UserModel,
    /**
     * Store the currency model
     */
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    @NotNull
    val currency: CurrencyModel,

    /**
     * Store the balance of the currency
     */
    @Column(name = "balance", nullable = false, precision = 12, scale = 6)
    @NotNull
    val balance: BigDecimal,

    /**
     * Timestamp where the last transaction was made
     */
    @Column(name = "last_transaction_at", nullable = true)
    val lastTransactionAt: LocalDateTime?,
)