package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The ExecutionThreadMetrics class represents the metrics of an execution thread. It keeps track of
 * the total number of promises, completed promises, and failed promises.
 */
public class ExecutionThreadMetrics {
    private final AtomicLong totalPromises = new AtomicLong();
    private final AtomicLong completedPromises = new AtomicLong();
    private final AtomicLong failedPromises = new AtomicLong();

    /**
     * Retrieves the total number of promises tracked by the ExecutionThreadMetrics object.
     *
     * @return The total number of promises as a long value.
     */
    public long getTotalPromises() {
        return totalPromises.get();
    }

    public void incrementTotalPromises() {
        totalPromises.incrementAndGet();
    }

    /**
     * Retrieves the total number of completed promises tracked by the ExecutionThreadMetrics
     * object.
     *
     * @return The total number of completed promises as a long value.
     */
    public long getCompletedPromises() {
        return completedPromises.get();
    }

    public void incrementCompletedPromises() {
        completedPromises.incrementAndGet();
    }

    public void incrementFailedPromises() {
        failedPromises.incrementAndGet();
    }

    @Override
    public String toString() {
        return "ExecutionThreadMetrics{"
                + "totalPromises="
                + totalPromises
                + ", completedPromises="
                + completedPromises
                + ", failedPromises="
                + failedPromises
                + '}';
    }
}
