package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.exception.Messages.ORIGIN_URL_NOT_FOUND_BY_SHORT_URL
import com.moflow.urlshortener.exception.NotFoundException
import com.moflow.urlshortener.repository.ShortUrlRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class OriginUrlFindServiceTest(
    @MockK private val shortUrlRepository: ShortUrlRepository,
) {
    @InjectMockKs
    private lateinit var sut: OriginUrlFindService

    @Test
    fun `sut should return short url dto when short url is given`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortenUrl = "asdfxC2"
        val shortUrl = ShortUrl(originUrl = originUrl, shortUrl = shortenUrl)
        every { shortUrlRepository.findByShortUrl(shortenUrl) } returns shortUrl

        // Act
        val actual = sut.findByShortUrl(shortenUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortUrl", shortenUrl)

        verify {
            shortUrlRepository.findByShortUrl(shortenUrl)
        }
    }

    @Test
    fun `sut should return null when short url is not exists`() {
        // Arrange
        val shortenUrl = "asdklxc"
        every { shortUrlRepository.findByShortUrl(shortenUrl) } returns null

        // Act & Assert
        val exception = assertThrows(NotFoundException::class.java) {
            sut.findByShortUrl(shortenUrl)
        }
        assertThat(exception.code).isEqualTo(ORIGIN_URL_NOT_FOUND_BY_SHORT_URL.first)
        verify {
            shortUrlRepository.findByShortUrl(shortenUrl)
        }
    }
}
