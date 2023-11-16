package com.moflow.urlshortener.cron

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.service.SnowflakeGenerator
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RedisSchedulerTest(
    @MockK private val snowflakeGenerator: SnowflakeGenerator,
    @MockK private val managedCache: ManagedCache,
) {
    @InjectMockKs
    private lateinit var sut: RedisScheduler

    @Test
    fun `sut delete serial number key`() {
        // Arrange
        val serialNumberKey = "SerialNumberCacheKey"
        every { snowflakeGenerator.getSerialNumberKey() } returns serialNumberKey
        every { managedCache.delete(serialNumberKey) } returns true

        // Act
        sut.deleteSerialNumberKey()

        // Assert
        verify {
            snowflakeGenerator.getSerialNumberKey()
            managedCache.delete(serialNumberKey)
        }
    }
}
