package io.felipepoliveira.coinex.security.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.ext.toDate
import io.felipepoliveira.coinex.ext.toLocalDateTime
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.security.tokens.dto.EmailConfirmationTokenPayload
import io.felipepoliveira.coinex.security.tokens.dto.PasswordRecoveryTokenPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EmailConfirmationTokenHandler @Autowired constructor(
    private val contextualDependencies: ContextualDependencies
) {
    companion object {
        const val ISSUER: String = "coinex"
        const val SUBJECT: String = "coinex:tokens:emailConfirmation:1.0"
    }

    fun issue(user: UserModel, expiresAt: LocalDateTime): EmailConfirmationTokenPayload {
        val userUuid = user.uuid.toString()
        val issuedAt = LocalDateTime.now()
        val token = JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withIssuedAt(issuedAt.toDate())
            .withExpiresAt(expiresAt.toDate())
            .withClaim("uuid", userUuid)
            .sign(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken()))

        return EmailConfirmationTokenPayload(
            issuedAt = issuedAt,
            userUuid = userUuid,
            expiresAt = expiresAt,
            token = token
        )
    }

    fun validateAndParse(token: String): EmailConfirmationTokenPayload {
        val jwtPayload = JWT.require(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken()))
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .build()
            .verify(token)

        return EmailConfirmationTokenPayload(
            issuedAt = jwtPayload.issuedAt.toLocalDateTime(),
            userUuid = jwtPayload.getClaim("uuid").asString(),
            expiresAt = jwtPayload.expiresAt.toLocalDateTime(),
            token = token
        )
    }
}