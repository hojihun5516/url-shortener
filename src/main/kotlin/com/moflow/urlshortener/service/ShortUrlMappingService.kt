package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.lock.DistributedLock
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
@Transactional
class ShortUrlMappingService(
    private val shortUrlFindService: ShortUrlFindService,
    private val shortUrlKeyGenerator: ShortUrlKeyGenerator,
    private val shortUrlRepository: ShortUrlRepository,
) {
    @DistributedLock(key = "#originUrl", waitTime = 10L, leaseTime = 2L, timeUnit = TimeUnit.SECONDS)
    fun mapShortUrl(originUrl: String): ShortUrlDto {
        val existsShortUrl = shortUrlFindService.findByOriginUrl(originUrl)
        if (existsShortUrl != null) {
            return existsShortUrl
        }

        val shortKey = shortUrlKeyGenerator.generate()
        val shortUrl = shortUrlRepository.save(ShortUrl(originUrl, shortKey))
        return shortUrl.toShortUrlDto()
    }
}
