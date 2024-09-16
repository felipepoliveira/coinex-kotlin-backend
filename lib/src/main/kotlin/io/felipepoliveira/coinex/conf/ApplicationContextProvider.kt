package io.felipepoliveira.coinex.conf

interface ApplicationContextProvider {
    fun getApplicationContext(): ApplicationContext
}

enum class ApplicationContext {
    Development
}