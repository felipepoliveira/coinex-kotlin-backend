package io.felipepoliveira.coinex.services.dto.customer

import io.felipepoliveira.coinex.models.Language
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateCustomerDTO(
    /**
     * The name of the customer that will be applied in the customer account
     */
    @field:NotNull
    @field:Size(min = 2, max = 60)
    val name: String,

    /**
     * The preferred language of the user
     */
    @field:NotNull
    val preferredLanguage: Language,

    /**
     * The primary email of the created customer account
     */
    @field:Email
    @field:NotNull
    val primaryEmail: String,

    /**
     * The password of the customer
     */
    @field:NotNull
    @field:Size(min = 8)
    val password: String,
)