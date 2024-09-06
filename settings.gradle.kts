plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "coinex-kotlin-backend"

// Include subprojects
include("lib", "platformApi")

// Set up project directory names if they are different from the default
project(":lib").projectDir = file("lib")
project(":platformApi").projectDir = file("platformApi")