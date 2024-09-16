package io.felipepoliveira.coinex.conf

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties
import javax.sql.DataSource

interface ContextualDependencies {

    /**
     * Return an DataSource instance containing a connection object that will be used in the relational database
     */
    fun relationalDatabaseDataSource(): DataSource

    /**
     * Return an instance containing properties used in relational database configuration
     */
    fun relationalDatabaseProperties(): Properties
}

class DevelopmentDependencies : ContextualDependencies {
    override fun relationalDatabaseDataSource(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/coinex?createDatabaseIfNotExists=true"
        config.username = "root"
        config.password = "root132"
        config.driverClassName = com.mysql.cj.jdbc.Driver::class.java.name
        config.maximumPoolSize = 10
        config.minimumIdle = 5
        config.connectionTimeout = 30_000
        config.idleTimeout = 600_000
        config.maxLifetime = 1_800_000

        return HikariDataSource(config)
    }

    override fun relationalDatabaseProperties(): Properties {
        val properties = Properties()
        properties.setProperty("hibernate.dialect", org.hibernate.dialect.MySQLDialect::class.java.name)
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");

        return properties
    }

}