package com.moflow.urlshortener.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class ShortUrlCreateRequest(
    @field:NotBlank(message = "The URL must not be blank")
    @field:URL(message = "Invalid URL format")
    val originUrl: String,
)
