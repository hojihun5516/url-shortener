package com.moflow.urlshortener.lock

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Aspect
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient,
) {
    private val logger = KotlinLogging.logger { }

    @Around("@annotation(com.moflow.urlshortener.lock.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)
        val distributedLockKey = CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            distributedLock.key
        )
        val key = "$REDISSON_LOCK_PREFIX${method.name}-$distributedLockKey"
        val lock: RLock = redissonClient.getLock(key)

        return try {
            if (!lock.tryLock(distributedLock.waitTime, distributedLock.leaseTime, distributedLock.timeUnit)) {
                logger.warn { "Failed to acquire lock -$key" }
                return null
            }
            logger.debug { "lock key: $key" }
            joinPoint.proceed()
        } finally {
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                lock.unlock()
                logger.debug { "unlock key: $key" }
            }
        }
    }

    companion object {
        private const val REDISSON_LOCK_PREFIX = "lock:"
    }
}
