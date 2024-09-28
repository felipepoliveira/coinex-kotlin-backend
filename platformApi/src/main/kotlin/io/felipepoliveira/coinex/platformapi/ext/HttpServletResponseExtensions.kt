package io.felipepoliveira.coinex.platformapi.ext

import io.felipepoliveira.coinex.platformapi.security.AuthenticationStatus
import jakarta.servlet.http.HttpServletResponse

fun HttpServletResponse.addAuthenticationStatusHeader(authenticationStatus: AuthenticationStatus) {
    this.addHeader("X-Auth-Status", authenticationStatus.toString())
    this.addHeader("X-Auth-Code", authenticationStatus.code.toString())
}