package io.felipepoliveira.coinex.platformapi.security

import io.felipepoliveira.coinex.services.ServiceRequester
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Duration
import java.time.LocalDateTime

class RequestClient(
    private val credentials: Any,
    private val serviceRequester: ServiceRequester,
) : Authentication {

    constructor(authenticationTokenPayload: AuthenticationTokenPayload): this(
        authenticationTokenPayload,
        authenticationTokenPayload.asServiceRequester()
    ) {
        calculateRoles(authenticationTokenPayload.asServiceRequester(), authenticationTokenPayload.issuedAt)
    }

    private val roles : MutableCollection<GrantedAuthority> = mutableListOf()

    private fun calculateRoles(serviceRequester: ServiceRequester, sessionCreationDate: LocalDateTime) {
        roles.addAll(calculateStlRoles(sessionCreationDate))
    }

    private fun calculateStlRoles(sessionCreationDate: LocalDateTime): MutableCollection<out GrantedAuthority> {
        val roles = mutableListOf<GrantedAuthority>()

        val sessionDuration = Duration.between(sessionCreationDate, LocalDateTime.now())
        if (sessionDuration.toHours() <= 12) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_RECENT_AUTHENTICATION))
        }
        else if (sessionDuration.toHours() <= 1) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_LAST_HOUR))
        }
        else if (sessionDuration.toMinutes() <= 15) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_MOST_RECENT))
        }

        return roles
    }

    override fun getName(): String  = serviceRequester.userUuid.toString()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    override fun getCredentials(): Any = credentials

    override fun getDetails(): Any = serviceRequester

    override fun getPrincipal(): Any = serviceRequester

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}