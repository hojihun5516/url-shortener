package com.moflow.urlshortener.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import redis.embedded.RedisServer

@TestConfiguration
class EmbeddedRedisConfig(
    @Value("\${spring.data.redis.host:localhost}")
    private val redisHost: String,
    @Value("\${spring.data.redis.port}")
    private val redisPort: Int
) {
    private val redisServer = RedisServer(redisPort)

    @PostConstruct
    fun startRedisServer() {
        redisServer.start()
    }

    @PreDestroy
    fun stopRedisServer() {
        redisServer.stop()
    }

    @Bean
    fun redissonConnectionFactory() = RedissonConnectionFactory(redissonClient())

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config();
        config.useSingleServer()
            .setAddress("redis://$redisHost:$redisPort")
        return Redisson.create(config);
    }
}
