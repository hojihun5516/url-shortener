package com.moflow.urlshortener.service

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class SnowflakeTimestampGenerator {

    fun currentTimestamp(): String {
        val bitOperand = ((1L shl TIMESTAMP_BITS) - 1)
        val timeMillis = Instant.now().toEpochMilli()
        return ((timeMillis - EPOCH) and bitOperand).toString() //and 연산자를 통해 필요한 41비트만 사용
    }

    companion object {
        private const val EPOCH = 1609459200000L // 2021-01-01T00:00:00Z
        private const val TIMESTAMP_BITS = 41
    }
}
