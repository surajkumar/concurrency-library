package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.threads.ExecutionThread;
import io.github.surajkumar.concurrency.metrics.ThreadPoolMetrics;

public interface ThreadPool {
    ExecutionThread borrow();

    void returnToPool(ExecutionThread executionThread);

    void shutdown();
    boolean isShutdown();

    ThreadPoolMetrics getMetrics();
}
