package org.example.io.felipepoliveira.coinex.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "organization", indexes = [
    Index(columnList = "code", name = "UI_code_AT_organization")
])
class OrganizationModel(
    /**
     * Store the organization Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    /**
     * The user that owns the organization
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    val owner: UserModel,

    /**
     * Store the organization unique code
     */
    @Column(name = "code", nullable = false)
    @NotNull
    val code: String,

    /**
     * Store the organization name
     */
    @Column(name = "name", nullable = false, length = 60)
    @NotNull
    @Size(min = 1, max = 60)
    val name: String,
)