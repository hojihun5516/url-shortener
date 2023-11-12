package com.moflow.urlshortener.controller

import com.moflow.urlshortener.dto.ShortUrlCreateRequest
import com.moflow.urlshortener.dto.ShortUrlCreateResponse
import com.moflow.urlshortener.extension.ShortUrlDtoExtension.toShortUrlCreateResponse
import com.moflow.urlshortener.service.OriginUrlFindService
import com.moflow.urlshortener.service.ShortUrlMappingService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class ShortUrlController(
    private val shortUrlMappingService: ShortUrlMappingService,
    private val originUrlFindService: OriginUrlFindService,
) {
    @PostMapping("/short-url")
    @ResponseStatus(HttpStatus.CREATED)
    fun map(@RequestBody @Validated shortUrlCreateRequest: ShortUrlCreateRequest): ShortUrlCreateResponse {
        val shortUrlDto = shortUrlMappingService.mapShortUrl(shortUrlCreateRequest.originUrl)
        return shortUrlDto.toShortUrlCreateResponse()
    }

    @GetMapping("/redirect/{shortUrl}")
    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    fun redirect(@PathVariable shortUrl: String): RedirectView {
        val shortUrlDto = originUrlFindService.findByShortUrl(shortUrl)
        val redirectView = RedirectView()
        redirectView.url = shortUrlDto.originUrl
        return redirectView;
    }
}
