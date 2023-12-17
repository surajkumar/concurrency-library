package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ChannelMetrics {
    private final AtomicLong sentMessages = new AtomicLong(0);
    private final AtomicInteger registeredObservers = new AtomicInteger(0);

    public long getSentMessages() {
        return sentMessages.get();
    }

    public long getRegisteredObservers() {
        return registeredObservers.get();
    }

    public void incrementObserverCount() {
        registeredObservers.incrementAndGet();
    }

    public void decrementObserverCount() {
        registeredObservers.decrementAndGet();
    }

    public void incrementSentMessages() {
        sentMessages.incrementAndGet();
    }
}
