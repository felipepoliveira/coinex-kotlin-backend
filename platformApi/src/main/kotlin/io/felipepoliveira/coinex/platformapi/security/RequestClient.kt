package io.felipepoliveira.coinex.platformapi.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.felipepoliveira.coinex.services.ServiceRequester
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Duration
import java.time.LocalDateTime

class RequestClient(
    @JsonIgnore val requestCredentials: Any,
    @JsonProperty(value = "user") val serviceRequester: ServiceRequester,
) : Authentication {

    /**
     * The roles attribute to the Request Client
     */
    @JsonProperty(value = "roles")
    private val roles : MutableCollection<GrantedAuthority> = mutableListOf()

    /**
     * The STL level attribute to the request\
     */
    @JsonProperty(value = "stl")
    private var stl: StlRole = StlRoles.NONE

    constructor(authenticationTokenPayload: AuthenticationTokenPayload): this(
        authenticationTokenPayload,
        authenticationTokenPayload.asServiceRequester()
    ) {
        calculateRoles(authenticationTokenPayload.asServiceRequester(), authenticationTokenPayload.issuedAt)
    }

    private fun calculateRoles(serviceRequester: ServiceRequester, sessionCreationDate: LocalDateTime) {
        roles.addAll(calculateStlRoles(sessionCreationDate))
    }

    private fun calculateStlRoles(sessionCreationDate: LocalDateTime): MutableCollection<out GrantedAuthority> {
        val roles = mutableListOf<GrantedAuthority>()

        val sessionDuration = Duration.between(sessionCreationDate, LocalDateTime.now())
        if (sessionDuration.toHours() <= 12) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_RECENT_AUTHENTICATION))
            stl = StlRoles.RECENT_AUTHENTICATION
        }
        if (sessionDuration.toHours() <= 1) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_LAST_HOUR))
            stl = StlRoles.LAST_HOUR
        }
        if (sessionDuration.toMinutes() <= 15) {
            roles.add(SimpleGrantedAuthority(SecurityRoles.STL_MOST_RECENT))
            stl = StlRoles.STL_MOST_RECENT
        }

        return roles
    }

    @JsonIgnore
    override fun getName(): String  = serviceRequester.userUuid.toString()

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    @JsonIgnore
    override fun getCredentials(): Any = requestCredentials

    @JsonIgnore
    override fun getDetails(): Any = serviceRequester

    @JsonIgnore
    override fun getPrincipal(): Any = this

    @JsonIgnore
    override fun isAuthenticated(): Boolean = true

    @JsonIgnore
    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}