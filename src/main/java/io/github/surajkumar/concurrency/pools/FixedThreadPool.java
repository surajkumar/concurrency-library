package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.exceptions.ExecutionMachineShutdownException;
import io.github.surajkumar.concurrency.metrics.ThreadPoolMetrics;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The FixedThreadPool class is an implementation of the ThreadPool interface. It represents a
 * fixed-sized thread pool that allows borrowing and returning of ExecutionThreads. The class uses a
 * Pool object to manage the threads and tracks metrics using the ThreadPoolMetrics class.
 */
public class FixedThreadPool implements ThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedThreadPool.class);
    private final ThreadPoolMetrics threadPoolMetrics = new ThreadPoolMetrics();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Pool pool;

    public FixedThreadPool(Pool pool) {
        this.pool = pool;
        for (int i = 0; i < pool.getInitialCapacity(); i++) {
            pool.add(ExecutionThread.createStarted());
        }
        threadPoolMetrics.setInitialCapacity(pool.getInitialCapacity());
        threadPoolMetrics.setActiveThreads(0);
    }

    @Override
    public ExecutionThread borrow() {
        if (!running.get()) {
            throw new ExecutionMachineShutdownException();
        }
        threadPoolMetrics.setAvailableThreads(pool.getSize() - 1);
        threadPoolMetrics.setActiveThreads(threadPoolMetrics.getActiveThreads() + 1);
        if (pool.getPoolOptions().isWaitFor()) {
            return pool.take();
        } else {
            return pool.get();
        }
    }

    @Override
    public void returnToPool(ExecutionThread executionThread) {
        if (!running.get()) {
            LOGGER.warn(
                    "ExecutionMachine has been shutdown but received a returnToPool request."
                            + " Retiring ExecutionThread");
            executionThread.setRunning(false);
            return;
        }
        LOGGER.trace("Returning {} to pool", executionThread);
        pool.add(executionThread);
        threadPoolMetrics.setAvailableThreads(threadPoolMetrics.getAvailableThreads() + 1);
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void shutdown() {
        running.set(false);
        while (!pool.isEmpty()) {
            ExecutionThread executionThread = pool.get();
            executionThread.setRunning(false);
            executionThread.getThread().interrupt();
        }
        pool.getLoaned()
                .forEach(
                        t -> {
                            t.setRunning(false);
                        });
        pool.getLoaned().clear();
    }

    @Override
    public boolean isShutdown() {
        return running.get();
    }

    public ThreadPoolMetrics getMetrics() {
        return threadPoolMetrics;
    }
}
