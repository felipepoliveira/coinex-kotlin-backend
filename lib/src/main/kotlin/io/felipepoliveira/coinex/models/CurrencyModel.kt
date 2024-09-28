package io.felipepoliveira.coinex.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "currency", indexes = [
    Index(columnList = "name", name = "UI_uuid_AT_currency")
])
class CurrencyModel(
    /**
     * Store the ID of the currency model
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long,
    /**
     * Store the organization that owns the currency
     */
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    @NotNull
    val organization: OrganizationModel,

    /**
     * Store the UUID of the currency model
     */
    @Column(name = "uuid", nullable = false)
    @NotNull
    val uuid: UUID,
    /**
     * Store the name of the currency model
     */
    @Column(name = "name", nullable = false, length = 40)
    @NotNull
    @Size(min = 1, max = 40)
    val name: String,
)