package io.felipepoliveira.coinex.platformapi.conf

import io.felipepoliveira.coinex.conf.ApplicationContext
import io.felipepoliveira.coinex.conf.ApplicationContextProvider
import io.felipepoliveira.coinex.conf.CoinexConfiguration
import io.felipepoliveira.coinex.platformapi.security.AuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class PlatformApiContextProvider : ApplicationContextProvider {
    override fun getApplicationContext(): ApplicationContext = ApplicationContext.Development
}

@Configuration
@ComponentScans(
    value = [
        ComponentScan("io.felipepoliveira.coinex.platformapi.rest.v1"),
        ComponentScan("io.felipepoliveira.coinex.platformapi.security")
    ]
)
@EnableMethodSecurity(securedEnabled = true) // Enable @Secured annotation
@EnableWebSecurity
@Import(value = [CoinexConfiguration::class, PlatformApiContextProvider::class])
class PlatformApiConfiguration {

    /**
     * Authentication filter used to authenticate and authorize users
     */
    @Autowired
    private lateinit var authenticationFilter: AuthenticationFilter

    /**
     * Configure HTTP Spring Security Context
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/v1/*/public/**").permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling { eh ->
                eh.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                eh.accessDeniedHandler(customAccessDeniedHandler())
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    /**
     * Custom AccessDeniedHandler to return 403 Forbidden for insufficient roles
     */
    @Bean
    fun customAccessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { _: HttpServletRequest, response: HttpServletResponse, _: AccessDeniedException ->
            response.status = HttpStatus.FORBIDDEN.value()
            response.addHeader("X-Error", "Access Denied: You do not have sufficient permissions to access this resource.")
        }
    }
}
