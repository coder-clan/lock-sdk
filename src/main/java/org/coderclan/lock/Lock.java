package org.coderclan.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotated by @Lock need to obtain a lock to proceed.
 * The lock is determined by {@link #type()} and LockKey(which is evaluated by EL Expression {@link #key()})
 * Lock will automatic release after {@link #maxLockTime} seconds.
 * Make sure {@link #maxLockTime()}  is far greater than, the method execution time plus {@link #retryDelay()} multiple {@link #retryTimes()}.
 * Created by aray.chou.cn(at)gmail(dot)com on 8/15/2018.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Lock {
    /**
     * EL expression to generate Lock key (which should be unique in each type).
     * Parameters of the method is put in the el context with key "arg" as an Array, the first parameter can be access by "arg[0]", and the second one is arg[1]
     *
     * @return EL Expression to generate Lock key. eg: "arg[0].data.id"
     */
    String key();

    /**
     * @return Lock Type, should be unique.
     */
    String type();

    /**
     * @return Max lock time, unit: Second. If holding lock exceeded its value, automatic unlock.
     */
    int maxLockTime();

    /**
     * @return times that retry to obtain lock. Do NOT retry if it is Zero.
     */
    int retryTimes();

    /**
     * @return delay time between each retry, unit: ms
     */
    int retryDelay();

    /**
     * Whether execute annotated method when error/exception occurs. default false.
     *
     * @return
     */
    boolean proceedWhileError() default false;

    /**
     * Don't unlock it after business method finish execution. Hold lock until it expires while {@link #maxLockTime()} exceeded.
     *
     * @return
     */
    boolean keepLock() default false;

}
