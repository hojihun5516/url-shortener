package com.moflow.urlshortener

 import com.moflow.urlshortener.config.EmbeddedRedisConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
@Import(EmbeddedRedisConfig::class)
class UrlShortenerApplicationTests {

    @Test
    fun contextLoads() {
    }

}
