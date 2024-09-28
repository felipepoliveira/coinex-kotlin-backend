package io.felipepoliveira.coinex.platformapi.conf

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.felipepoliveira.coinex.conf.ApplicationContext
import io.felipepoliveira.coinex.conf.ApplicationContextProvider
import io.felipepoliveira.coinex.conf.CoinexConfiguration
import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.platformapi.security.AuthenticationFilter
import io.felipepoliveira.coinex.platformapi.security.AuthenticationTokenHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class PlatformApiContextProvider: ApplicationContextProvider {
    override fun getApplicationContext(): ApplicationContext = ApplicationContext.Development
}


@Configuration
@ComponentScans(value = [
    ComponentScan("io.felipepoliveira.coinex.platformapi.rest.v1"),
    ComponentScan("io.felipepoliveira.coinex.platformapi.security"),
])
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
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/v1/*/public/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { eh ->
                eh.authenticationEntryPoint(unauthorizedEntryPoint())
            }
        return http.build()
    }

    /**
     * Create an object that will handle authenticationEntryPoint error. This object is used in securityFilterChain
     * on the Spring Security configuration script
     */
    private fun unauthorizedEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { _: HttpServletRequest, response: HttpServletResponse, _: AuthenticationException ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not identified in request")
        }
    }
}