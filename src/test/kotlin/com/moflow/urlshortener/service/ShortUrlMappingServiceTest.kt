package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ShortUrlMappingServiceTest(
    @MockK private val shortUrlKeyGenerator: ShortUrlKeyGenerator,
    @MockK private val shortUrlRepository: ShortUrlRepository,
    @MockK private val shortUrlFindService: ShortUrlFindService,
) {
    @InjectMockKs
    private lateinit var sut: ShortUrlMappingService

    @Test
    fun `sut should return short url dto when origin url already map`() {
        // Arrange
        val originUrl = "https://temp.com/asdf"
        val shortKey = "ade21CZ"
        val shortUrl = ShortUrl(originUrl, shortKey)
        val expected = shortUrl.toShortUrlDto()

        every { shortUrlFindService.findByOriginUrl(originUrl) } returns expected

        // Act
        val actual = sut.mapShortUrl(originUrl)

        // Assert
        assertThat(actual).isEqualTo(expected)
        verify {
            shortUrlFindService.findByOriginUrl(originUrl)
            shortUrlKeyGenerator wasNot called
        }
    }

    @Test
    fun `sut should map short key and origin url when origin url does not mapped`() {
        // Arrange
        val originUrl = "https://temp.com/asdf"
        val shortKey = "ade21CZ"
        val shortUrl = ShortUrl(originUrl, shortKey)
        val expected = shortUrl.toShortUrlDto()

        every { shortUrlFindService.findByOriginUrl(originUrl) } returns null
        every { shortUrlKeyGenerator.generate() } returns shortKey
        every { shortUrlRepository.save(any()) } returns shortUrl

        // Act
        val actual = sut.mapShortUrl(originUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", expected.originUrl)
            .hasFieldOrPropertyWithValue("shortKey", expected.shortKey)
        verify {
            shortUrlFindService.findByOriginUrl(originUrl)
            shortUrlKeyGenerator.generate()
            shortUrlRepository.save(any())
        }
    }
}