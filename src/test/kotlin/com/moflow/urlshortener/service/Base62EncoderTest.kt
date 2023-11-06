package com.moflow.urlshortener.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Base62EncoderTest {
    private val sut = Base62Encoder()

    @Test
    fun `sut should encode base62 when binary string is given`() {
        // Arrange
        val binaryString = "1010011100100100011100110001111100001" // 예시 Snowflake 키
        val expected = "1zWOe77"

        // Act
        val actual = sut.encode(binaryString)

        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}
