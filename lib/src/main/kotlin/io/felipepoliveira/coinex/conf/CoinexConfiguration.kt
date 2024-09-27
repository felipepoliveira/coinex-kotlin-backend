package io.felipepoliveira.coinex.conf

import io.felipepoliveira.coinex.cache.CacheRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
@ComponentScans(value = [
    ComponentScan("io.felipepoliveira.coinex.cache"),
    ComponentScan("io.felipepoliveira.coinex.dao.hibernate"),
    ComponentScan("io.felipepoliveira.coinex.mail"),
    ComponentScan("io.felipepoliveira.coinex.security"),
    ComponentScan("io.felipepoliveira.coinex.services"),
])
@EnableTransactionManagement(proxyTargetClass = true)
open class CoinexConfiguration {

    @Bean
    fun cacheRepository(contextualDependencies: ContextualDependencies): CacheRepository = contextualDependencies.cacheRepository()

    @Bean
    fun contextualDependencies(applicationContextProvider: ApplicationContextProvider): ContextualDependencies {
        return when (applicationContextProvider.getApplicationContext()) {
            ApplicationContext.Development -> DevelopmentDependencies()
        }
    }

    @Bean
    fun entityManagerFactory(contextualDependencies: ContextualDependencies): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactory = LocalContainerEntityManagerFactoryBean()
        entityManagerFactory.dataSource = contextualDependencies.relationalDatabaseDataSource()
        entityManagerFactory.setJpaProperties(contextualDependencies.relationalDatabaseProperties())
        entityManagerFactory.setPackagesToScan("io.felipepoliveira.coinex.models")

        val vendorAdapter = HibernateJpaVendorAdapter()
        entityManagerFactory.jpaVendorAdapter = vendorAdapter

        return entityManagerFactory
    }

    @Bean
    fun localValidatorFactoryBean() = LocalValidatorFactoryBean()

    @Bean
    fun mailProvider(contextualDependencies: ContextualDependencies) = contextualDependencies.mailProvider()

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        return transactionManager
    }
}