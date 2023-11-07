package com.moflow.urlshortener.service

import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.extension.ShortUrlExtension.toShortUrlDto
import com.moflow.urlshortener.repository.ShortUrlRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ShortUrlFindService(
    private val shortUrlRepository: ShortUrlRepository,
) {
    fun findByOriginUrl(originUrl: String): ShortUrlDto? {
        return shortUrlRepository.findByOriginUrl(originUrl)?.toShortUrlDto()
    }
}
