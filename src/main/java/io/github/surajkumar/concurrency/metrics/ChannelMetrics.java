package io.github.surajkumar.concurrency.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The ChannelMetrics class is responsible for tracking metrics related to a Channel. It keeps track
 * of the number of sent messages and the number of registered observers.
 */
public class ChannelMetrics {
    private final AtomicLong sentMessages = new AtomicLong(0);
    private final AtomicInteger registeredObservers = new AtomicInteger(0);

    /**
     * The ChannelMetrics class is responsible for tracking metrics related to a Channel. It keeps
     * track of the number of sent messages and the number of registered observers.
     */
    public ChannelMetrics() {}

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

    /**
     * Increments the count of registered observers in the ChannelMetrics class. This method is
     * called when a new observer is registered to receive messages from the channel.
     */
    public void incrementObserverCount() {
        registeredObservers.incrementAndGet();
    }

    /**
     * Decrements the count of registered observers in the ChannelMetrics class. This method is
     * called when an observer is deregistered from receiving messages from the channel. The count
     * is decremented by 1.
     */
    public void decrementObserverCount() {
        registeredObservers.decrementAndGet();
    }

    /**
     * Increments the count of sent messages in the ChannelMetrics class. This method is called when
     * a message is sent through a Channel.
     */
    public void incrementSentMessages() {
        sentMessages.incrementAndGet();
    }
}
