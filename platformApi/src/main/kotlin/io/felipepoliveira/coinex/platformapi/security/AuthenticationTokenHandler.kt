package io.felipepoliveira.coinex.platformapi.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.ext.toDate
import io.felipepoliveira.coinex.ext.toLocalDateTime
import io.felipepoliveira.coinex.models.UserModel
import io.felipepoliveira.coinex.security.tokens.dto.PasswordRecoveryTokenPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class AuthenticationTokenHandler @Autowired constructor(
    private val contextualDependencies: ContextualDependencies
) {

    companion object {
        const val ISSUER: String = "coinex"
        const val SUBJECT: String = "coinex:tokens:authenticationToken:1.0"
    }

    fun issue(user: UserModel, expiresAt: LocalDateTime): PasswordRecoveryTokenPayload {
        val userUuid = user.uuid.toString()
        val issuedAt = LocalDateTime.now()
        val token = JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withIssuedAt(issuedAt.toDate())
            .withExpiresAt(expiresAt.toDate())
            .withClaim("uuid", userUuid)
            .sign(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken()))

        return PasswordRecoveryTokenPayload(
            issuedAt = issuedAt,
            userUuid = userUuid,
            expiresAt = expiresAt,
            token = token
        )
    }

    fun validateAndParse(token: String): PasswordRecoveryTokenPayload {
        val jwtPayload = JWT.require(Algorithm.HMAC512(contextualDependencies.secretKeyForPasswordRecoveryToken()))
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .build()
            .verify(token)

        return PasswordRecoveryTokenPayload(
            issuedAt = jwtPayload.issuedAt.toLocalDateTime(),
            userUuid = jwtPayload.getClaim("uuid").asString(),
            expiresAt = jwtPayload.expiresAt.toLocalDateTime(),
            token = token
        )
    }
}