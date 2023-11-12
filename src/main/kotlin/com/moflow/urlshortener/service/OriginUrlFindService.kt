package com.moflow.urlshortener.service

import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.exception.Messages.ORIGIN_URL_NOT_FOUND_BY_SHORT_URL
import com.moflow.urlshortener.exception.NotFoundException
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OriginUrlFindService(
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun findByShortUrl(shortUrl: String): ShortUrlDto {
        return shortUrlRepository.findByShortUrl(shortUrl)?.toShortUrlDto()
            ?: throw NotFoundException(ORIGIN_URL_NOT_FOUND_BY_SHORT_URL, shortUrl)
    }
}
