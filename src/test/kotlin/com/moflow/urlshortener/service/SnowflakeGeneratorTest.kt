package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles

//@ActiveProfiles("test")
@ExtendWith( MockKExtension::class)
class SnowflakeGeneratorTest(
    @MockK private val managedCache: ManagedCache,
    @MockK private val snowflakeTimestampGenerator: SnowflakeTimestampGenerator,
) {
    private val dataCenterId = "0001"
    private val appName = "url-shortener"
    private val serverId = "0001"
    private lateinit var sut: SnowflakeGenerator

    @BeforeEach
    fun setup() {
        sut = SnowflakeGenerator(
            managedCache,
            snowflakeTimestampGenerator,
            dataCenterId,
            appName,
            serverId,
        )
    }

    @Test
    fun `sut should generate snowflake key`() {
        // Arrange
        val serialNumber = 123
        val currentTimestamp = "19958400000"
        val SNOWFLAKE_SERIAL_NUMBER_REDIS_KEY = "snowflake-serial"
        every { managedCache.increment(any()) } returns serialNumber
        every { snowflakeTimestampGenerator.currentTimestamp() } returns currentTimestamp
        val expectedKey = "$currentTimestamp$dataCenterId$serverId$serialNumber"

        // Act
        val actual = sut.generate()

        // Assert
        assertThat(actual).isEqualTo(expectedKey)
        verify {
            managedCache.increment("$appName${SNOWFLAKE_SERIAL_NUMBER_REDIS_KEY}")
            snowflakeTimestampGenerator.currentTimestamp()
        }
    }
}
