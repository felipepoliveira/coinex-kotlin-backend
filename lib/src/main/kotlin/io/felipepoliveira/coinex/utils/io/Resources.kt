package io.felipepoliveira.coinex.utils.io

import io.felipepoliveira.coinex.models.Language
import java.io.InputStream

sealed class Resources {
    companion object {

        fun getLocalizedResourceInputStream(localizedResourcePath: String, language: Language): InputStream {
            val normalizedLocalizedResourcePath = localizedResourcePath
                .replace("[language]", language.toString().lowercase())
                .replace("[lang]", language.toString().lowercase())
            return getResourceInputStream(normalizedLocalizedResourcePath)
        }

        fun getResourceInputStream(resourcePath: String): InputStream {
            return Resources::class.java.getResourceAsStream(resourcePath) ?:
                throw Exception("Resource identified by path '$resourcePath' was not found")
        }
    }
}