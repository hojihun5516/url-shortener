package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.exception.Messages.ORIGIN_URL_NOT_FOUND_BY_SHORT_URL
import com.moflow.urlshortener.exception.NotFoundException
import com.moflow.urlshortener.repository.ShortUrlRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(MockKExtension::class)
class OriginUrlFindServiceTest(
    @MockK private val shortUrlRepository: ShortUrlRepository,
    @MockK private val managedCache: ManagedCache,
) {
    @InjectMockKs
    private lateinit var sut: OriginUrlFindService

    @Test
    @DisplayName("캐시에 데이터가 있다면, short url dto를 반환한다")
    fun `sut should return short url dto when short url is given and cache is exists`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortenUrl = "asdfxC2"
        val redisKey = "short-url:$shortenUrl"
        every { managedCache.get<String>(redisKey) } returns originUrl

        // Act
        val actual = sut.findByShortUrl(shortenUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortUrl", shortenUrl)

        verify {
            shortUrlRepository wasNot Called
            managedCache.get<String>(redisKey)
        }
    }

    @Test
    @DisplayName("캐시에 데이터가 없고, 등록된 short url이 있다면 short url dto를 반환한다")
    fun `sut should return short url dto when short url is given and cache is not exists`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortenUrl = "asdfxC2"
        val redisKey = "short-url:$shortenUrl"
        val shortUrl = ShortUrl(originUrl = originUrl, shortUrl = shortenUrl)
        every { managedCache.get<String>(redisKey) } returns null
        every { shortUrlRepository.findByShortUrl(shortenUrl) } returns shortUrl
        justRun { managedCache.set(redisKey, originUrl, 5L, TimeUnit.DAYS) }

        // Act
        val actual = sut.findByShortUrl(shortenUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortUrl", shortenUrl)

        verify {
            shortUrlRepository.findByShortUrl(shortenUrl)
            managedCache.get<String>(redisKey)
            managedCache.set(redisKey, originUrl, 5L, TimeUnit.DAYS)
        }
    }

    @Test
    @DisplayName("캐시에 데이터가 없고, 등록된 short url이 없다면 Exception을 발생시킨다")
    fun `sut throw not found exception when short url is not exists`() {
        // Arrange
        val shortenUrl = "asdfxC2"
        val redisKey = "short-url:$shortenUrl"

        every { managedCache.get<String>(redisKey) } returns null
        every { shortUrlRepository.findByShortUrl(shortenUrl) } returns null

        // Act & Assert
        val exception = assertThrows(NotFoundException::class.java) {
            sut.findByShortUrl(shortenUrl)
        }
        assertThat(exception.code).isEqualTo(ORIGIN_URL_NOT_FOUND_BY_SHORT_URL.first)
        verify {
            shortUrlRepository.findByShortUrl(shortenUrl)
            managedCache.get<String>(redisKey)
        }
    }
}
