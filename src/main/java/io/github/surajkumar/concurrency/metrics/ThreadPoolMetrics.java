package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolMetrics {
    private final AtomicInteger initialCapacity = new AtomicInteger();
    private final AtomicInteger activeThreads = new AtomicInteger();
    private final AtomicInteger availableThreads = new AtomicInteger();

    public void clear() {
        initialCapacity.set(0);
        activeThreads.set(0);
        availableThreads.set(0);
    }

    public int getInitialCapacity() {
        return initialCapacity.get();
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity.set(initialCapacity);
    }

    public int getActiveThreads() {
        return activeThreads.get();
    }

    public void setActiveThreads(int activeThreads) {
        this.activeThreads.set(activeThreads);
    }

    public int getAvailableThreads() {
        return availableThreads.get();
    }

    public void setAvailableThreads(int availableThreads) {
        this.availableThreads.set(availableThreads);
    }

    @Override
    public String toString() {
        return "ThreadPoolMetrics{" +
                "initialCapacity=" + initialCapacity +
                ", activeThreads=" + activeThreads +
                ", availableThreads=" + availableThreads +
                '}';
    }
}
