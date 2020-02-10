package org.coderclan.lock;

/**
 * Indicate that an Exception has been countered while obtaining or releasing a lock.
 * Created by aray.chou.cn(at)gmail(dot)com on 8/15/2018.
 */
public class LockException extends Exception {
    public LockException() {
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
