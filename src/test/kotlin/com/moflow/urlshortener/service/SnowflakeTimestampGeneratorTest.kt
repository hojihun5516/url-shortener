package com.moflow.urlshortener.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
class SnowflakeTimestampGeneratorTest {

    @InjectMockKs
    private lateinit var sut: SnowflakeTimestampGenerator

    @Test
    fun `sut should generate 41 bits timestamp`() {
        // Arrange
        val instant = Instant.parse("2021-08-20T00:00:00Z")
        val currentMillis = instant.toEpochMilli() // 2021-08-20T00:00:00Z
        val epoch = 1609459200000L
        val timestampBits = 41
        mockkStatic(Instant::class)
        every { Instant.now() } returns instant

        val expected = ((currentMillis - epoch) and ((1L shl timestampBits) - 1)).toString()

        // Act
        val actual = sut.currentTimestamp()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}
