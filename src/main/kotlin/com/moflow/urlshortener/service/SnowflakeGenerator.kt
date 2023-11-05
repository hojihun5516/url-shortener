package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SnowflakeGenerator(
    private val managedCache: ManagedCache,
    private val snowflakeTimestampGenerator: SnowflakeTimestampGenerator,
    @Value("spring.application.data-center-id")
    private val dataCenterId: String,
    @Value("spring.application.name")
    private val appName: String,
    @Value("spring.application.server-id")
    private val serverId: String,
) {
    fun generate(): String {
        val serialNumber = managedCache.increment(appName + SERIAL_NUMBER_REDIS_KEY).toString(RADIX)
        val currentTimestamp = snowflakeTimestampGenerator.currentTimestamp().toString(RADIX)
        val snowflakeKey = SIGN_BIT + currentTimestamp + dataCenterId + serverId + serialNumber
        return snowflakeKey
    }

    companion object {
        private const val RADIX = 2
        private const val SERIAL_NUMBER_REDIS_KEY = "snowflake-serial"
        private const val SIGN_BIT = "1"
    }
}
