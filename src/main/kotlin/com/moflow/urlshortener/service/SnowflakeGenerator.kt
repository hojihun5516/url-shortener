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
        val serialNumber = managedCache.increment(appName + SNOWFLAKE_SERIAL_NUMBER_REDIS_KEY)
        val currentTimestamp = snowflakeTimestampGenerator.currentTimestamp()
        val snowflakeKey = currentTimestamp + dataCenterId + serverId + serialNumber
        return snowflakeKey
    }

    companion object {
        private const val SNOWFLAKE_SERIAL_NUMBER_REDIS_KEY = "snowflake-serial"
    }
}
