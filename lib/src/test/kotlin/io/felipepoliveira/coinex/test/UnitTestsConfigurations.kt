package io.felipepoliveira.coinex.test

import io.felipepoliveira.coinex.conf.ContextualDependencies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*
import javax.sql.DataSource

@Configuration
@ComponentScans(value = [
    ComponentScan("io.felipepoliveira.coinex.services"),
    ComponentScan("io.felipepoliveira.coinex.test.unit.mocks.dao"),
])
class UnitTestsConfigurations : ContextualDependencies {

    @Bean
    fun localValidatorFactoryBean() = LocalValidatorFactoryBean()

    override fun relationalDatabaseDataSource(): DataSource {
        throw NotImplementedError("relationalDatabaseDataSource can not be used in unit tests context")
    }

    override fun relationalDatabaseProperties(): Properties {
        throw NotImplementedError("relationalDatabaseProperties can not be used in unit tests context")
    }
}