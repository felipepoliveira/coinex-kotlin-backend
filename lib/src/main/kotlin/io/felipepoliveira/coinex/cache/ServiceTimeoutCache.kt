package io.felipepoliveira.coinex.cache

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.time.Duration

@Component
class ServiceTimeoutCache @Autowired constructor(
    private val cacheRepository: CacheRepository
) {

    fun executeOnTimeout(tag: String, timeout: Duration, callback: () -> Unit) {
        // if the item is on the cache repository
        if (cacheRepository.getString(tag) != null) {
            return
        }

        // execute the call back
        callback()

        // add the tag on timeout
        cacheRepository.putString(tag, "service-timeout", timeout)
    }

}