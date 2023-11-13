package com.moflow.urlshortener.service

import org.springframework.stereotype.Component

@Component
class ShortUrlKeyGenerator(
    private val snowflakeGenerator: SnowflakeGenerator,
    private val base62Encoder: Base62Encoder,
) {

    fun generate(): String {
        val snowflakeKey = snowflakeGenerator.generate()
        return base62Encoder.encode(snowflakeKey)
    }
}
