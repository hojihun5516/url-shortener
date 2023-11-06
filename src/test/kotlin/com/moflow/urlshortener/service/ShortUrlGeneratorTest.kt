package com.moflow.urlshortener.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ShortUrlGeneratorTest(
    @MockK private val snowflakeGenerator: SnowflakeGenerator,
    @MockK private val base62Encoder: Base62Encoder,
) {
    @InjectMockKs
    private lateinit var sut: ShortUrlGenerator

    @Test
    fun `sut should generate shortUrl`() {
        // Arrange
        val snowflakeKey = "10101010101010101010101010101010"
        val expected = "az83EZY"

        every { snowflakeGenerator.generate() } returns snowflakeKey
        every { base62Encoder.encode(snowflakeKey) } returns expected

        // Act
        val actual = sut.generate()

        // Assert
        assertThat(actual).isEqualTo(expected)
        verify {
            snowflakeGenerator.generate()
            base62Encoder.encode(snowflakeKey)
        }
    }
}
