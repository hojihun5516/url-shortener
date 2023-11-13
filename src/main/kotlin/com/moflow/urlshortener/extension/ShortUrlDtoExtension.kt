package com.moflow.urlshortener.extension

import com.moflow.urlshortener.dto.ShortUrlCreateResponse
import com.moflow.urlshortener.dto.ShortUrlDto

object ShortUrlDtoExtension {
    fun ShortUrlDto.toShortUrlCreateResponse(): ShortUrlCreateResponse {
        return ShortUrlCreateResponse(
            shortKey = shortKey,
        )
    }
}
