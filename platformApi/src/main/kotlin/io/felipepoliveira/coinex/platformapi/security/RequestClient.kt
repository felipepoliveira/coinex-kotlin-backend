package io.felipepoliveira.coinex.platformapi.security

import io.felipepoliveira.coinex.services.ServiceRequester
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class RequestClient(
    private val credentials: Any,
    private val serviceRequester: ServiceRequester,
) : Authentication {

    constructor(authenticationTokenPayload: AuthenticationTokenPayload): this(
        authenticationTokenPayload,
        authenticationTokenPayload.asServiceRequester()
    )

    override fun getName(): String  = serviceRequester.userUuid.toString()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf() //TODO implement this

    override fun getCredentials(): Any = credentials

    override fun getDetails(): Any = serviceRequester

    override fun getPrincipal(): Any = serviceRequester

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}