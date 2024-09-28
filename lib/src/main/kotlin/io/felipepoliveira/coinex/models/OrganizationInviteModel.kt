package io.felipepoliveira.coinex.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "organization_invite", indexes = [
    Index(columnList = "uuid", name = "UI_uuid_AT_organization_invite"),
    Index(columnList = "organization_id, recipient_email", name = "UI_organization_id_AND_recipient_email_AT_organization_invite"),
])
class OrganizationInviteModel(
    /**
     * The id of the organization invite model
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long,

    /**
     * The UUID of the organization invite model
     */
    @Column(name = "uuid", nullable = false)
    @NotNull
    val uuid: UUID,

    /**
     * The organization where the invites refer to
     */
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    @NotNull
    val organization: OrganizationModel,

    /**
     * The e-mail that will receive the invite
     */
    @Column(name = "recipient_email", nullable = false)
    @Email
    @NotNull
    val recipientEmail: String,

    /**
     * Store the timestamp when the invite was created
     */
    @Column(name = "created_at", nullable = false)
    @NotNull
    val createdAt: LocalDateTime,
)