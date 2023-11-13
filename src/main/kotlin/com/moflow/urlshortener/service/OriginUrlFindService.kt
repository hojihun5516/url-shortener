package com.moflow.urlshortener.service

import com.moflow.urlshortener.cache.ManagedCache
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.exception.Messages.ORIGIN_URL_NOT_FOUND_BY_SHORT_KEY
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
    fun findByShortKey(shortKey: String): ShortUrlDto {
        val redisKey = REDIS_KEY_PREFIX + shortKey
        managedCache.get<String>(redisKey)?.let {
            return ShortUrlDto(originUrl = it, shortKey = shortKey)
        }

        val shortUrlDto = shortUrlRepository.findByShortKey(shortKey)?.toShortUrlDto()
            ?: throw NotFoundException(ORIGIN_URL_NOT_FOUND_BY_SHORT_KEY, shortKey)
        managedCache.set(redisKey, shortUrlDto.originUrl, REDIS_CACHE_TTL, TimeUnit.DAYS)
        return shortUrlDto
    }

    companion object {
        private const val REDIS_KEY_PREFIX = "short-url:"
        private const val REDIS_CACHE_TTL = 5L
    }
}
