package io.felipepoliveira.coinex.platformapi.security

import io.felipepoliveira.coinex.platformapi.ext.addAuthenticationStatusHeader
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val authenticationTokenHandler: AuthenticationTokenHandler,
) : OncePerRequestFilter(){
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        // Get the authentication token from the request
        val token = getAuthenticationToken(request)
        if (token == null) {
            exitWithAuthenticationStatus(AuthenticationStatus.USER_NOT_IDENTIFIED, request, response, filterChain)
            return
        }

        // Get the authentication token payload
        val authenticationTokenPayload: AuthenticationTokenPayload
        try {
            authenticationTokenPayload = authenticationTokenHandler.validateAndParse(token)
        } catch (e: Exception) {
            exitWithAuthenticationStatus(AuthenticationStatus.INVALID_CREDENTIALS, request, response, filterChain)
            return
        }

        // Set the authenticated user as authenticated
        SecurityContextHolder.getContext().authentication = RequestClient(authenticationTokenPayload)
        exitWithAuthenticationStatus(AuthenticationStatus.AUTHENTICATION_SUCCESS, request, response, filterChain)
    }

    private fun exitWithAuthenticationStatus(
        authenticationStatus: AuthenticationStatus, request: HttpServletRequest,
        response: HttpServletResponse, filterChain: FilterChain) {

        response.addAuthenticationStatusHeader(authenticationStatus)
        filterChain.doFilter(request, response)

    }

    private fun getAuthenticationToken(request: HttpServletRequest): String? {
        val headerValue = request.getHeader("Authorization") ?: return null;

        // If the header value does not start with "Bearer " return null
        if (!headerValue.startsWith("Bearer ")) {
            return null
        }

        // Return the token part from the header
        return headerValue.split(" ")[1]
    }
}

enum class AuthenticationStatus(
    val code: Int
) {
    AUTHENTICATION_SUCCESS(0),
    USER_NOT_IDENTIFIED(100),
    INVALID_CREDENTIALS(101),
}