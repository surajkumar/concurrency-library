package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class ExecutionThreadMetrics {
    private final AtomicLong totalPromises = new AtomicLong();
    private final AtomicLong completedPromises = new AtomicLong();
    private final AtomicLong failedPromises = new AtomicLong();

    public long getTotalPromises() {
        return totalPromises.get();
    }

    public void incrementTotalPromises() {
        totalPromises.incrementAndGet();
    }

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
        return "ExecutionThreadMetrics{" +
                "totalPromises=" + totalPromises +
                ", completedPromises=" + completedPromises +
                ", failedPromises=" + failedPromises +
                '}';
    }
}
