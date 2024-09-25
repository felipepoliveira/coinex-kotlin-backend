package io.felipepoliveira.coinex.cache

import redis.clients.jedis.JedisPool
import kotlin.time.Duration

/**
 * Represents a cache repository using a Redis instance
 */
class RedisCacheRepository(
    /**
     * Store and make a connection to a redis instance
     */
    private val jedisPool: JedisPool,
) : CacheRepository {

    override fun getString(key: String): String? {
        jedisPool.resource.use { jedis ->
            return jedis.get(key)
        }
    }

    override fun putString(key: String, value: String, timeToLive: Duration?) {
        jedisPool.resource.use { jedis ->
            if (timeToLive != null) {
                jedis.setex(key, timeToLive.inWholeSeconds, value)
            }
            else {
                jedis.set(key, value)
            }
        }
    }
}