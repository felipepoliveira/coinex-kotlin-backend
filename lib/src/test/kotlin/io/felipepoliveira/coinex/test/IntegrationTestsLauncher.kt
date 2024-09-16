package io.felipepoliveira.coinex.test

import io.felipepoliveira.coinex.conf.ApplicationContext
import io.felipepoliveira.coinex.conf.ApplicationContextProvider
import io.felipepoliveira.coinex.conf.CoinexConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(CoinexConfiguration::class)
class IntegrationTestConfiguration : ApplicationContextProvider {
    override fun getApplicationContext(): ApplicationContext {
        return ApplicationContext.Development
    }

}

fun main() {
    runApplication<IntegrationTestConfiguration>()
}