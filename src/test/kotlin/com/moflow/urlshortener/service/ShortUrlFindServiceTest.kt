package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.repository.ShortUrlRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(MockKExtension::class)
class ShortUrlFindServiceTest(
    @MockK private val shortUrlRepository: ShortUrlRepository,
    @MockK private val managedCache: ManagedCache,
) {
    @InjectMockKs
    private lateinit var sut: ShortUrlFindService

    @Test
    fun `sut should return short url dto when short key is exists in rdbms`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortKey = "asdfxC2"
        val shortUrl = ShortUrl(originUrl = originUrl, shortKey = shortKey)
        val redisKey = "origin-url:$originUrl"

        every { managedCache.get<String>(redisKey) } returns null
        every { shortUrlRepository.findByOriginUrl(originUrl) } returns shortUrl
        justRun { managedCache.set(redisKey, shortKey, 3L, TimeUnit.DAYS) }

        // Act
        val actual = sut.findByOriginUrl(originUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortKey", shortKey)

        verify {
            managedCache.get<String>(redisKey)
            shortUrlRepository.findByOriginUrl(originUrl)
            managedCache.set(redisKey, shortKey, 3L, TimeUnit.DAYS)
        }
    }

    @Test
    fun `sut should return short url dto when short key is exists in redis`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val shortKey = "asdfxC2"
        val redisKey = "origin-url:$originUrl"

        every { managedCache.get<String>(redisKey) } returns shortKey

        // Act
        val actual = sut.findByOriginUrl(originUrl)

        // Assert
        assertThat(actual)
            .hasFieldOrPropertyWithValue("originUrl", originUrl)
            .hasFieldOrPropertyWithValue("shortKey", shortKey)

        verify {
            managedCache.get<String>(redisKey)
        }

        verify(exactly = 0) {
            shortUrlRepository.findByOriginUrl(originUrl)
            managedCache.set(redisKey, shortKey, 3L, TimeUnit.DAYS)
        }
    }

    @Test
    fun `sut should return null when short key is not exists`() {
        // Arrange
        val originUrl = "https://ASDF.com"
        val redisKey = "origin-url:$originUrl"
        every { managedCache.get<String>(redisKey) } returns null
        every { shortUrlRepository.findByOriginUrl(originUrl) } returns null

        // Act
        val actual = sut.findByOriginUrl(originUrl)

        // Assert
        assertThat(actual).isNull()
        verify {
            managedCache.get<String>(redisKey)
            shortUrlRepository.findByOriginUrl(originUrl)
        }

        verify(exactly = 0) {
            managedCache.set(redisKey, any(), 3L, TimeUnit.DAYS)
        }
    }

}