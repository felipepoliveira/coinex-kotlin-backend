package io.felipepoliveira.coinex.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "organization_member", indexes = [

])
class OrganizationMemberModel(
    /**
     * The id of the organization member model
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    /**
     * The user that is a member of the organization
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    val member: UserModel,

    /**
     * The organization where this member resides
     */
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    @NotNull
    val organization: OrganizationModel,

    /**
     * Store the date where the user becomes a member of the organization
     */
    @Column(name = "created_at", nullable = false)
    @NotNull
    val createdAt: LocalDateTime,

    /**
     * Store the granted roles of the organization member
     */
    @ElementCollection
    @CollectionTable(name = "organization_member_roles", joinColumns = [JoinColumn(name = "organization_member_id")])
    @Column(name = "role", nullable = false, length = 60)
    val grantedRoles: Set<String>,
)