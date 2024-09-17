package io.felipepoliveira.coinex.cache

import kotlin.time.Duration

interface CacheRepository {

    fun getString(key: String): String?

    fun putString(key: String, value: String, timeToLive: Duration? = null)

}