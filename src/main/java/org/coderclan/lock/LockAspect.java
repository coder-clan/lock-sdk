package org.coderclan.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Make {@link Lock} works.
 * Created by aray.chou.cn(at)gmail(dot)com on 8/15/2018.
 */
@Component
@Aspect
public class LockAspect {

    private static Logger logger = LoggerFactory.getLogger(LockAspect.class);


    @Around("@annotation(org.coderclan.lock.Lock)")
    public Object aroundService(ProceedingJoinPoint joinPoint) {


        Lock annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Lock.class);

        if (annotation == null) {
            return proceed(joinPoint);
        } else {
            Object lockKey = getLockKey(joinPoint, annotation);
            LockService lockService = LockUtil.getLockService();

            LockBean lockBean = LockBean.build()
                    .withLockType(annotation.type())
                    .withLockKey(lockKey)
                    .withMaxLockTime(annotation.maxLockTime())
                    .withRetryDelay(annotation.retryDelay())
                    .withRetryTime(annotation.retryTimes())
                    .withOwner(UUID.randomUUID().toString())
                    .withNotUnlock(annotation.keepLock())
                    .toLockBean();

            if (logger.isDebugEnabled()) {
                logger.debug("Try to obtain lock, lockBean={}", LockUtil.gson().toJson(lockBean));
            }

            boolean proceed;
            try {
                proceed = lockService.lock(lockBean);
            } catch (Exception e) {
                proceed = annotation.proceedWhileError();
                logger.error("Exception countered while getting lock. proceed=" + proceed, e);
            }

            try {
                if (proceed)
                    return proceed(joinPoint);
            } finally {
                try {
                    // It is NOT a big deal to release an un-existing lock.
                    // but the lock may exists even when exception countered while trying to lock.
                    if (lockBean.isKeepLock()) {
                        logger.debug("Don't unlock this lock since it annotated with notUnlock=true. lockBean={}", LockUtil.gson().toJson(lockBean));
                    } else {
                        lockService.unlock(lockBean);
                    }
                } catch (LockException e) {
                    logger.error("", e);
                }
            }
        }

        logger.info("Get lock failed, " + ((MethodSignature) joinPoint.getSignature()).getMethod() + " not executed !");
        throw new FailedToLockException("Get lock failed!");
    }

    private Object proceed(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Object getLockKey(ProceedingJoinPoint arg, Lock lock) {
        Expression exp = getExpression(arg, lock);
        IdempotentElRoot root = new IdempotentElRoot(arg.getArgs());
        Object idempotentId = exp.getValue(root);
        return idempotentId;
    }


    private static final ConcurrentHashMap<String, Expression> EL_EXPRESSION_CACHE = new ConcurrentHashMap<>();

    private Expression getExpression(ProceedingJoinPoint joinPoint, Lock lock) {

        String key = joinPoint.getSignature().getName();

        Expression exp = EL_EXPRESSION_CACHE.get(key);
        if (exp == null) {
            synchronized (EL_EXPRESSION_CACHE) {
                exp = EL_EXPRESSION_CACHE.get(key);
                if (exp == null) {
                    String idempotentIdEl = lock.key();
                    ExpressionParser parser = new SpelExpressionParser();
                    exp = parser.parseExpression(idempotentIdEl);
                    EL_EXPRESSION_CACHE.put(key, exp);
                }
            }
        }
        return exp;
    }

}
