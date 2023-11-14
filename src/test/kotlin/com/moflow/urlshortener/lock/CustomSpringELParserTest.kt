package com.moflow.urlshortener.lock

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CustomSpringELParserTest {
    private val sut = CustomSpringELParser

    @Test
    fun `sut return argument`() {
        // Arrange
        val parameterNames = arrayOf("originUrl")
        val args = arrayOf<Any>("https://naver.com")
        val key = "#originUrl"
        val expected = "https://naver.com"

        // Act
        val actual = sut.getDynamicValue(parameterNames, args, key)

        // Assert
        assertEquals(expected, actual)
    }
}
