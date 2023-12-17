package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.metrics.ThreadPoolMetrics;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

public interface ThreadPool {
    ExecutionThread borrow();

    void returnToPool(ExecutionThread executionThread);

    void shutdown();

    boolean isShutdown();

    ThreadPoolMetrics getMetrics();
}
