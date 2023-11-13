package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.repository.ShortUrlRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ShortUrlFindServiceTest(
    @MockK private val shortUrlRepository: ShortUrlRepository,
) {
    @InjectMockKs
    private lateinit var sut: ShortUrlFindService

    @Test
    fun `sut should return short url dto when short key is exists`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortKey = "asdfxC2"
        val shortUrl = ShortUrl(originUrl = originUrl, shortKey = shortKey)
        every { shortUrlRepository.findByOriginUrl(originUrl) } returns shortUrl

        // Act
        val actual = sut.findByOriginUrl(originUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortKey", shortKey)

        verify {
            shortUrlRepository.findByOriginUrl(originUrl)
        }
    }

    @Test
    fun `sut should return null when short key is not exists`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        every { shortUrlRepository.findByOriginUrl(originUrl) } returns null

        // Act
        val actual = sut.findByOriginUrl(originUrl)

        // Assert
        assertThat(actual).isNull()
        verify {
            shortUrlRepository.findByOriginUrl(originUrl)
        }
    }

}