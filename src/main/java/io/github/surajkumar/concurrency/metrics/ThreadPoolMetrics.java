package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents a set of metrics for a thread pool. It tracks the initial capacity, active threads, and available threads.
 *
 * The metrics can be accessed through getters and modified through setters. The class also provides a method to clear all metrics.
 */
public class ThreadPoolMetrics {
    private final AtomicInteger initialCapacity = new AtomicInteger();
    private final AtomicInteger activeThreads = new AtomicInteger();
    private final AtomicInteger availableThreads = new AtomicInteger();

    /**
     * Clears all metrics in the ThreadPoolMetrics object.
     *
     * This method sets the initial capacity, active threads, and available threads metrics to 0.
     */
    public void clear() {
        initialCapacity.set(0);
        activeThreads.set(0);
        availableThreads.set(0);
    }

    /**
     * Returns the initial capacity of the thread pool.
     *
     * @return the initial capacity of the thread pool
     */
    public int getInitialCapacity() {
        return initialCapacity.get();
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity.set(initialCapacity);
    }

    /**
     * Returns the number of active threads in the thread pool.
     *
     * @return the number of active threads in the thread pool
     */
    public int getActiveThreads() {
        return activeThreads.get();
    }

    /**
     * Sets the number of active threads in the thread pool.
     *
     * @param activeThreads the number of active threads to set
     */
    public void setActiveThreads(int activeThreads) {
        this.activeThreads.set(activeThreads);
    }

    /**
     * Returns the number of available threads in the thread pool.
     *
     * @return The number of available threads
     */
    public int getAvailableThreads() {
        return availableThreads.get();
    }

    /**
     * Sets the number of available threads in the thread pool. This method should be used to update the available threads metric
     * when a thread is borrowed from the pool.
     *
     * @param availableThreads The new number of available threads in the thread pool
     */
    public void setAvailableThreads(int availableThreads) {
        this.availableThreads.set(availableThreads);
    }

    @Override
    public String toString() {
        return "ThreadPoolMetrics{"
                + "initialCapacity="
                + initialCapacity
                + ", activeThreads="
                + activeThreads
                + ", availableThreads="
                + availableThreads
                + '}';
    }
}
