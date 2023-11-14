package com.moflow.urlshortener.lock

import java.util.concurrent.TimeUnit

/**
 * 분산 잠금을 위한 어노테이션.
 * 해당 어노테이션을 메서드에 적용하면 해당 메서드 실행 시 분산 잠금이 적용된다.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DistributedLock(
    /**
     * 잠금 키를 지정한다. Spring EL 표현식을 사용하여 동적인 값을 지정한다.
     * 예: "#paramName"
     * @return 잠금 키 문자열
     */
    val key: String = "",

    /**
     * 잠금을 획득하기 위해 대기할 최대 시간(초)을 지정.
     */
    val waitTime: Long = 5L,

    /**
     * 잠금을 보유할 최대 시간(초)을 지정.
     */
    val leaseTime: Long = 2L,

    /**
     * 잠금 시간을 나타내는 TimeUnit 설정
     */
    val timeUnit: TimeUnit = TimeUnit.SECONDS
)
