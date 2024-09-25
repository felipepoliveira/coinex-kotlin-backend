package io.felipepoliveira.coinex.test

import io.felipepoliveira.coinex.cache.CacheRepository
import io.felipepoliveira.coinex.conf.ContextualDependencies
import io.felipepoliveira.coinex.mail.MailContentType
import io.felipepoliveira.coinex.mail.MailProvider
import io.felipepoliveira.coinex.mail.MailRecipient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*
import javax.sql.DataSource
import kotlin.time.Duration

@Configuration
@ComponentScans(value = [
    ComponentScan("io.felipepoliveira.coinex.cache"),
    ComponentScan("io.felipepoliveira.coinex.mail"),
    ComponentScan("io.felipepoliveira.coinex.security"),
    ComponentScan("io.felipepoliveira.coinex.services"),
    ComponentScan("io.felipepoliveira.coinex.test.unit.mocks.dao"),
])
class UnitTestsConfigurations : ContextualDependencies {

    /**
     * Create a mocked cache repository
     */
    @Bean
    override fun cacheRepository(): CacheRepository = object : CacheRepository {
        override fun getString(key: String): String? = null
        override fun putString(key: String, value: String, timeToLive: Duration?) {}
    }

    @Bean
    fun localValidatorFactoryBean() = LocalValidatorFactoryBean()

    override fun accountsWebappUrl() = "MOCKED"

    @Bean
    override fun mailProvider() = object : MailProvider {
        override fun sendMail(
            title: String,
            content: ByteArray,
            contentType: MailContentType,
            vararg mailRecipients: MailRecipient
        ) {
            println("Sent mocked mail $title to $mailRecipients")
        }

    }

    override fun relationalDatabaseDataSource(): DataSource {
        throw NotImplementedError("relationalDatabaseDataSource can not be used in unit tests context")
    }

    override fun relationalDatabaseProperties(): Properties {
        throw NotImplementedError("relationalDatabaseProperties can not be used in unit tests context")
    }

    override fun secretKeyForPasswordRecoveryToken(): ByteArray = "SECRET_KEY_FOR_PWD_RCV_TOKEN".toByteArray()
}