package org.coderclan.lock;

/**
 * Indicate that obtaining lock failed.
 * Created by aray.chou.cn(at)gmail(dot)com on 9/10/2018.
 */
public class FailedToLockException extends RuntimeException {
    public FailedToLockException() {
    }

    public FailedToLockException(String message) {
        super(message);
    }

    public FailedToLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToLockException(Throwable cause) {
        super(cause);
    }

    public FailedToLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
