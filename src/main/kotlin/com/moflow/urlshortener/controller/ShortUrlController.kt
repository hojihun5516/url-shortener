package com.moflow.urlshortener.controller

import com.moflow.urlshortener.dto.ShortUrlCreateRequest
import com.moflow.urlshortener.dto.ShortUrlCreateResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShortUrlController {

    @PostMapping("/short-url")
    fun shortUrl(@RequestBody @Validated shortUrlCreateRequest: ShortUrlCreateRequest): ShortUrlCreateResponse {
        return ShortUrlCreateResponse("tempShortUrl")
    }
}
