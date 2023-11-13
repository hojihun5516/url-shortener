package com.moflow.urlshortener.extension

import com.moflow.urlshortener.domain.ShortUrl
import com.moflow.urlshortener.dto.ShortUrlDto

object ShortUrlExtension {
    fun ShortUrl.toShortUrlDto(): ShortUrlDto {
        return ShortUrlDto(
            originUrl = originUrl,
            shortKey = shortKey,
        )
    }
}
