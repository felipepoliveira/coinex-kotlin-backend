package io.felipepoliveira.coinex.security.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.ext.toDate
import io.felipepoliveira.coinex.ext.toLocalDateTime
import io.felipepoliveira.coinex.models.UserModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class PrimaryEmailChangeTokenHandler @Autowired constructor(
    private val contextualDependencies: ContextualDependencies,
) {

    companion object {
        const val ISSUER: String = "coinex"
        const val SUBJECT: String = "coinex:tokens:primaryEmailChange:1.0"
    }

    fun issue(user: UserModel, newEmail: String, secretCode: String, expiresAt: LocalDateTime): PrimaryEmailChangeTokenPayload {
        val userUuid = user.uuid
        val issuedAt = LocalDateTime.now()
        val token = JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withIssuedAt(issuedAt.toDate())
            .withExpiresAt(expiresAt.toDate())
            .withClaim("uuid", userUuid.toString())
            .withClaim("newEmail", newEmail)
            .sign(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken() + secretCode.toByteArray()))

        return PrimaryEmailChangeTokenPayload(
            newEmail,
            issuedAt,
            expiresAt,
            userUuid,
            token,
        )
    }

    fun validateAndParse(token: String, secretCode: String): PrimaryEmailChangeTokenPayload {
        val jwtPayload = JWT.require(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken() + secretCode.toByteArray()))
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .build()
            .verify(token)

        return PrimaryEmailChangeTokenPayload(
            newEmail = jwtPayload.getClaim("newEmail").asString(),
            issuedAt = jwtPayload.issuedAt.toLocalDateTime(),
            expiresAt = jwtPayload.expiresAt.toLocalDateTime(),
            userUuid = UUID.fromString(jwtPayload.getClaim("uuid").asString()),
            token = token,
        )
    }

}

data class PrimaryEmailChangeTokenPayload(
    /**
     * The new email associated in the token
     */
    val newEmail: String,
    /**
     * Timestamp when the token was issued
     */
    val issuedAt: LocalDateTime,
    /**
     * Timestamp when the token will expire
     */
    val expiresAt: LocalDateTime,
    /**
     * The UUID of the user that requested the operation
     */
    val userUuid: UUID,
    /**
     * The token
     */
    val token: String,
)