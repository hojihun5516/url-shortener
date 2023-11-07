package com.moflow.urlshortener.service

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ShortUrlMappingService(
    private val shortUrlFindService: ShortUrlFindService,
    private val shortUrlGenerator: ShortUrlGenerator,
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun mapShortUrl(originUrl: String): ShortUrlDto {
        val existsShortUrl = shortUrlFindService.findByOriginUrl(originUrl)
        if(existsShortUrl !=null ){
            return existsShortUrl
        }

        val shortenUrl = shortUrlGenerator.generate()
        val shortUrl = shortUrlRepository.save(ShortUrl(originUrl, shortenUrl))
        return shortUrl.toShortUrlDto()
    }
}
