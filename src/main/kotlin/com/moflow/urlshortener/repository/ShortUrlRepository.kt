package com.moflow.urlshortener.repository

import com.moflow.urlshortener.domain.ShortUrl
import org.springframework.data.jpa.repository.JpaRepository

interface ShortUrlRepository : JpaRepository<ShortUrl, Long> {
    fun findByOriginUrl(originUrl: String): ShortUrl?
    fun findByShortUrl(shortUrl: String): ShortUrl?
}
