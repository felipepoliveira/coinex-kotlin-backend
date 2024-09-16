package io.felipepoliveira.coinex.conf

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
    ComponentScan("io.felipepoliveira.coinex.dao.hibernate"),
    ComponentScan("io.felipepoliveira.coinex.services")
])
@EnableTransactionManagement
open class CoinexConfiguration {

    @Bean
    fun contextualDependencies(applicationContextProvider: ApplicationContextProvider): ContextualDependencies {
        return when (applicationContextProvider.getApplicationContext()) {
            ApplicationContext.Development -> DevelopmentDependencies()
        }
    }

    @Bean
    fun entityManager(entityManagerFactory: EntityManagerFactory): EntityManager {
        return entityManagerFactory.createEntityManager()
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
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        return transactionManager
    }
}