package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.exception.Messages.ORIGIN_URL_NOT_FOUND_BY_SHORT_URL
import com.moflow.urlshortener.exception.NotFoundException
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
@Transactional(readOnly = true)
class OriginUrlFindService(
    private val shortUrlRepository: ShortUrlRepository,
    private val managedCache: ManagedCache,
) {
    fun findByShortUrl(shortUrl: String): ShortUrlDto {
        val redisKey = REDIS_KEY_PREFIX + shortUrl
        managedCache.get<String>(redisKey)?.let {
            return ShortUrlDto(originUrl = it, shortUrl = shortUrl)
        }

        val shortUrlDto = shortUrlRepository.findByShortUrl(shortUrl)?.toShortUrlDto()
            ?: throw NotFoundException(ORIGIN_URL_NOT_FOUND_BY_SHORT_URL, shortUrl)
        managedCache.set(redisKey, shortUrlDto.originUrl, REDIS_CACHE_TTL, TimeUnit.DAYS)
        return shortUrlDto
    }

    companion object {
        private const val REDIS_KEY_PREFIX = "short-url:"
        private const val REDIS_CACHE_TTL = 5L
    }
}
