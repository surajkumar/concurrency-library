package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The ChannelMetrics class is responsible for tracking metrics related to a Channel.
 * It keeps track of the number of sent messages and the number of registered observers.
 */
public class ChannelMetrics {
    private final AtomicLong sentMessages = new AtomicLong(0);
    private final AtomicInteger registeredObservers = new AtomicInteger(0);

    /**
     * Returns the total number of sent messages.
     *
     * @return The total number of sent messages.
     */
    public long getSentMessages() {
        return sentMessages.get();
    }

    /**
     * Returns the total number of registered observers.
     *
     * @return The total number of registered observers.
     */
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
