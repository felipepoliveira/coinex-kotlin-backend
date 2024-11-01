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

repositories {
    mavenCentral()
}

allOpen {
    // Marks all below to be all open
    annotation("org.springframework.context.annotation.Configuration")
    annotation("org.springframework.web.bind.annotation.RestController")
    annotation("jakarta.persistence.Entity")
}

dependencies {
    // Add a reference to /lib
    implementation(project(":lib"))

    // https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt:4.4.0")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security
    implementation("org.springframework.boot:spring-boot-starter-security:3.3.4")

}
