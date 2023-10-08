package com.moflow.urlshortener.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.moflow.urlshortener.dto.ShortUrlCreateRequest
import com.moflow.urlshortener.dto.ShortUrlCreateResponse
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
) {

    @Test
    @DisplayName("원본 URL을 받아서 단축 URL을 리턴한다")
    fun `sut return short url when origin url is given`() {
        // Arrange
        val originUrl = "https://google.com"
        val shortUrlCreateRequest = ShortUrlCreateRequest(originUrl)
        val shortUrlCreateResponse = ShortUrlCreateResponse("tempShortUrl")

        // Act & Assert
        mockMvc.perform(
            post("/short-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortUrlCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortUrl").value(shortUrlCreateResponse.shortUrl))
    }
}