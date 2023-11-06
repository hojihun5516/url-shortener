package com.moflow.urlshortener.service

import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class Base62Encoder {
    fun encode(binaryString: String): String {
        var number = BigInteger(binaryString, 2)
        val encoded = StringBuilder()

        while (number > BigInteger.ZERO) {
            val index = number.mod(BigInteger.valueOf(BASE_62)).toInt()
            encoded.insert(0, BASE_62_CHARS[index])
            number = number.divide(BigInteger.valueOf(BASE_62))
        }

        return encoded.toString()
    }

    companion object {
        private const val BASE_62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val BASE_62 = 62L
    }
}
