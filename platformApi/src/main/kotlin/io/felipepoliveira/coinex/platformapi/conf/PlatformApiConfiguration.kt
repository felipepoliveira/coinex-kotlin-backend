package io.felipepoliveira.coinex.platformapi.conf

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.felipepoliveira.coinex.conf.ApplicationContext
import io.felipepoliveira.coinex.conf.ApplicationContextProvider
import io.felipepoliveira.coinex.conf.CoinexConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@ComponentScans(value = [
    ComponentScan("io.felipepoliveira.coinex.platformapi.rest.v1"),
    ComponentScan("io.felipepoliveira.coinex.platformapi.security"),
])
@EnableWebSecurity
@Import(CoinexConfiguration::class)
class PlatformApiConfiguration : ApplicationContextProvider {
    /**
     * Return the application context
     */
    override fun getApplicationContext(): ApplicationContext = ApplicationContext.Development

    /**
     * Set the route access rules
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/**").permitAll()  // Allow all routes for testing
            }
        return http.build()
    }
}