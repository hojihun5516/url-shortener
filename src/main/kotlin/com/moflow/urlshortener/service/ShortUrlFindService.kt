package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
@Transactional(readOnly = true)
class ShortUrlFindService(
    private val shortUrlRepository: ShortUrlRepository,
    private val managedCache: ManagedCache,
) {
    fun findByOriginUrl(originUrl: String): ShortUrlDto? {
        val redisKey = REDIS_KEY_PREFIX + originUrl
        managedCache.get<String>(redisKey)?.let {
            return ShortUrlDto(originUrl = originUrl, shortKey = it)
        }

        return shortUrlRepository.findByOriginUrl(originUrl)?.let {
            managedCache.set(redisKey, it.shortKey, REDIS_CACHE_TTL, TimeUnit.DAYS)
            return it.toShortUrlDto()
        }
    }

    companion object {
        private const val REDIS_KEY_PREFIX = "origin-url:"
        private const val REDIS_CACHE_TTL = 3L
    }
}
