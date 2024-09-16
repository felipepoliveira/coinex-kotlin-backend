package io.felipepoliveira.coinex.test

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import io.kotest.spring.SpringAutowireConstructorExtension

class KotestConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(SpringExtension, SpringAutowireConstructorExtension)
}