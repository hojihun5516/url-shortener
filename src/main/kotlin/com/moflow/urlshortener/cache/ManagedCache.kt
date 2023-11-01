package com.moflow.urlshortener.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ManagedCache(
    private val objectMapper: ObjectMapper,
    private val redisTemplate: StringRedisTemplate,
) {
    private val valueOperations: ValueOperations<String, String> = redisTemplate.opsForValue()

    fun set(key: String, value: Any, ttl: Long, timeUnit: TimeUnit) {
        val serializedValue = objectMapper.writeValueAsString(CacheValue(value))
        valueOperations.set(key, serializedValue, ttl, timeUnit)
    }

    fun <T> get(key: String): T? {
        val value = valueOperations.get(key)

        return value?.let {
            val cacheValue: CacheValue<T> = objectMapper.readValue(it)
            cacheValue.value
        }
    }

    data class CacheValue<T>(val value: T)
}
