repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "2.0.0"

    // The `kotlin-allopen` plugin is used to open non-final classes, functions, and properties in Kotlin classes.
    // This is particularly useful in the context of frameworks like Spring and Hibernate, which require certain
    // methods and properties to be open for proxying and AOP (Aspect-Oriented Programming) to work correctly.
    // By applying this plugin and specifying the `@Entity` annotation for classes you want to open,
    // you allow these classes to be extended and overridden by proxy classes, ensuring proper functionality
    // with Spring and Hibernate.
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.20"
}

allOpen {
    // Marks all below to be all open
    annotation("org.springframework.context.annotation.Configuration")
    annotation("jakarta.persistence.Entity")
}

dependencies {
    // - Lib Core libraries
    // https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt:4.4.0")

    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation("com.zaxxer:HikariCP:5.1.0")

    // https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")

    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
    implementation("org.hibernate.orm:hibernate-core:6.6.0.Final")

    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa
    implementation("org.springframework.data:spring-data-jpa:3.3.4")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.3")

    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-crypto
    implementation("org.springframework.security:spring-security-crypto:6.3.3")

    // https://mvnrepository.com/artifact/mysql/mysql-connector-java
    implementation("mysql:mysql-connector-java:8.0.33")

    // https://mvnrepository.com/artifact/redis.clients/jedis
    implementation("redis.clients:jedis:5.1.5")

    // --- TEST IMPLEMENTATIONS
    // Kotest
    // https://mvnrepository.com/artifact/io.kotest/kotest-assertions-core-jvm
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    // https://mvnrepository.com/artifact/io.kotest/kotest-extensions-spring
    implementation("io.kotest:kotest-extensions-spring:4.4.3")
    // https://mvnrepository.com/artifact/io.kotest/kotest-runner-junit5-jvm
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")

    // Spring Boot Test
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-test
    testImplementation("org.springframework.boot:spring-boot-test:3.3.3")

    //Spring TestContext Framework
    // Spring Test supports the unit testing and integration testing of Spring components with JUnit or TestNG.
    // It provides consistent loading and caching of Spring ApplicationContexts and provides mock objects that you
    // can use to test your code in isolation
    // https://mvnrepository.com/artifact/org.springframework/spring-test
    testImplementation("org.springframework:spring-test:6.1.13")
}
