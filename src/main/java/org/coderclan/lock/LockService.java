package org.coderclan.lock;

public interface LockService {
    /**
     * Try to lock
     *
     * @param lock
     * @return true if lock success, false if lock failed.
     * @throws LockException
     */
    boolean lock(LockBean lock) throws LockException;

    /**
     * Release Lock. Only lock owned by {@link LockBean#owner} can release the lock.
     *
     * @param lock
     * @throws LockException
     */
    void unlock(LockBean lock) throws LockException;
}
