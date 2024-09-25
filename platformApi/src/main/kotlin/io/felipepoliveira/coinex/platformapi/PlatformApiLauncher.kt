package io.felipepoliveira.coinex.platformapi

import io.felipepoliveira.coinex.platformapi.conf.PlatformApiConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class]
)
@Import(PlatformApiConfiguration::class)
class PlatformApiLauncher

fun main(args: Array<String>) {
    SpringApplication.run(PlatformApiLauncher::class.java, *args)
}