package com.moflow.urlshortener.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.moflow.urlshortener.dto.ShortUrlCreateRequest
import com.moflow.urlshortener.dto.ShortUrlCreateResponse
import com.moflow.urlshortener.dto.ShortUrlDto
import com.moflow.urlshortener.service.ShortUrlMappingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@WebMvcTest(ShortUrlController::class)
class ShortUrlControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean private val shortUrlMappingService: ShortUrlMappingService,
) {

    @Test
    @DisplayName("원본 URL을 받아서 단축 URL을 리턴한다")
    fun `sut return short url when origin url is given`() {
        // Arrange
        val originUrl = "https://google.com"
        val shortUrl = "asdiew2Z"
        val shortUrlDto = ShortUrlDto(originUrl, shortUrl)
        val shortUrlCreateRequest = ShortUrlCreateRequest(originUrl)
        val shortUrlCreateResponse = ShortUrlCreateResponse(shortUrl)

        every { shortUrlMappingService.mapShortUrl(originUrl) } returns shortUrlDto

        // Act & Assert
        mockMvc.perform(
            post("/short-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortUrlCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.shortUrl").value(shortUrlCreateResponse.shortUrl))

        verify {
            shortUrlMappingService.mapShortUrl(originUrl)
        }
    }

    @Test
    @DisplayName("유효하지 않은 url은 에러를 발생시킨다")
    fun `sut should error url when url is not valid`() {
        // Arrange
        val originUrl = "asdfhttps://google.com"
        val shortUrlCreateRequest = ShortUrlCreateRequest(originUrl)

        // Act & Assert
        mockMvc.perform(
            post("/short-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortUrlCreateRequest))
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
    }
}