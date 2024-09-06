plugins {
    kotlin("jvm") version "2.0.0"
}

group = "io.felipepoliveira"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}