package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service

@Service
class ShortUrlMappingService(
    private val shortUrlGenerator: ShortUrlGenerator,
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun mapShortUrl(originUrl: String): ShortUrlDto {
        val existingShortUrl = shortUrlRepository.findByOriginUrl(originUrl)
        if (existingShortUrl != null) {
            return existingShortUrl.toShortUrlDto()
        }

        val shortenUrl = shortUrlGenerator.generate()
        val shortUrl = shortUrlRepository.save(ShortUrl(originUrl, shortenUrl))
        return shortUrl.toShortUrlDto()
    }
}
