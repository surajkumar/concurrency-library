package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.metrics.ThreadPoolMetrics;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

/** The ThreadPool interface represents a pool of execution threads. */
public interface ThreadPool {
    /**
     * Borrow method is used to request an available ExecutionThread from the ThreadPool.
     *
     * @return an ExecutionThread, null if no thread is available
     */
    ExecutionThread borrow();

    /**
     * Returns an ExecutionThread to the ThreadPool.
     *
     * @param executionThread the ExecutionThread to return to the pool
     */
    void returnToPool(ExecutionThread executionThread);

    /**
     * The shutdown method is used to gracefully shut down the ThreadPool. It stops the execution of
     * promises and releases any resources held by the ThreadPool.
     *
     * <p>If the ThreadPool has already been shutdown, calling this method has no effect.
     */
    void shutdown();

    /**
     * Returns a boolean indicating whether the ThreadPool has been shutdown or not.
     *
     * @return true if the ThreadPool has been shutdown, false otherwise
     */
    boolean isShutdown();

    /**
     * Retrieves the metrics of the thread pool.
     *
     * @return the metrics of the thread pool
     */
    ThreadPoolMetrics getMetrics();
}
