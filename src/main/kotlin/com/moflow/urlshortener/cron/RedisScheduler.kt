package com.moflow.urlshortener.cron

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.service.SnowflakeGenerator
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RedisScheduler(
    private val snowflakeGenerator: SnowflakeGenerator,
    private val managedCache: ManagedCache,
) {

    /**
     * 1초에 1번씩 serialNumber를 초기화한다
     */
    @Scheduled(cron = "* * * * * *")
    fun deleteSerialNumberKey() {
        val serialNumberCacheKey = snowflakeGenerator.getSerialNumberKey()
        managedCache.delete(serialNumberCacheKey)
    }
}
