package org.coderclan.lock;

import java.io.Serializable;
import java.util.Objects;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author aray.chou.cn(at)gmail(dot)com
 * @title <一句话说明功能>
 * @date 8/15/2018
 * @since <版本号>
 */

public class LockBean implements Serializable {
    private final String lockType;
    private final Object lockKey;
    private final int maxLockTime;
    private final int retryTime;
    private final int retryDelay;
    private final String owner;
    private final boolean keepLock;

    /**
     * Use {@link LockBean#build()} to get LockBean instance;
     *
     * @param lockType
     * @param lockKey
     * @param maxLockTime
     * @param retryTime
     * @param retryDelay
     * @param owner
     */
    public LockBean(String lockType, Object lockKey, int maxLockTime, int retryTime, int retryDelay, String owner, boolean keepLock) {
        this.lockType = lockType;
        this.lockKey = lockKey;
        this.maxLockTime = maxLockTime;
        this.retryTime = retryTime;
        this.retryDelay = retryDelay;
        this.owner = owner;
        this.keepLock = keepLock;
    }

    public static Builder build() {
        return new Builder();
    }

    /**
     * {@link Lock#type()}
     *
     * @return
     */
    public String getLockType() {
        return lockType;
    }

    /**
     * {@link Lock#key()}
     *
     * @return
     */
    public Object getLockKey() {
        return lockKey;
    }

    /**
     * {@link Lock#maxLockTime()}
     *
     * @return
     */
    public int getMaxLockTime() {
        return maxLockTime;
    }

    /**
     * {@link Lock#retryTimes()}
     *
     * @return
     */
    public int getRetryTime() {
        return retryTime;
    }

    /**
     * {@link Lock#retryDelay()}
     *
     * @return
     */
    public int getRetryDelay() {
        return retryDelay;
    }

    /**
     * @return The owner of the lock, should be unique.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return true if don't unlock it after business method finish execution. Hold lock until it expires while {@link Lock#maxLockTime()} exceeded.
     */
    public boolean isKeepLock() {
        return keepLock;
    }

    public static class Builder {
        private String lockType;
        private Object lockKey;
        private int maxLockTime;
        private int retryTime;
        private int retryDelay;
        private String owner;
        private boolean keepLock = false;

        public Builder withLockType(String lockType) {
            this.lockType = lockType;
            return this;
        }

        public Builder withLockKey(Object lockKey) {
            this.lockKey = lockKey;
            return this;
        }

        public Builder withMaxLockTime(int maxLockTime) {
            this.maxLockTime = maxLockTime;
            return this;
        }

        public Builder withRetryTime(int retryTime) {
            this.retryTime = retryTime;
            return this;
        }

        public Builder withRetryDelay(int retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public Builder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder withNotUnlock(boolean keepLock) {
            this.keepLock = keepLock;
            return this;
        }

        public LockBean toLockBean() {
            Objects.requireNonNull(this.lockType, "lockType must NOT be null");
            Objects.requireNonNull(this.lockKey, "lockKey must NOT be null");
            Objects.requireNonNull(this.owner, "owner must NOT be null");
            if (this.maxLockTime <= 0) {
                throw new IllegalArgumentException("maxLockTime must creator than zero");
            }
            if (this.retryTime < 0) {
                throw new IllegalArgumentException("retryTime must creator than or equal to zero");
            }
            if (this.retryDelay <= 0) {
                throw new IllegalArgumentException("retryDelay must creator than zero");
            }

            return new LockBean(this.lockType, this.lockKey, this.maxLockTime, this.retryTime, this.retryDelay, this.owner, this.keepLock);
        }
    }
}
