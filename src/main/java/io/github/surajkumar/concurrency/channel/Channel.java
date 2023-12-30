package io.github.surajkumar.concurrency.channel;

import io.github.surajkumar.concurrency.metrics.ChannelMetrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Channel class represents a channel that can send and receive messages.
 *
 * @param <T> The type of the message content.
 */
public class Channel<T> {
    private final List<ChannelObserver<T>> observers = Collections.synchronizedList(new ArrayList<>());
    private final ChannelMetrics metrics = new ChannelMetrics();

    /** The Channel class represents a channel that can send and receive messages. */
    public Channel() {}

    /**
     * Sends a message to all registered observers of the channel.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(Message<T> message) {
        metrics.incrementSentMessages();
        synchronized (this) {
            observers.forEach(
                    observer -> {
                        if (message.getSender() != observer) {
                            observer.onMessageReceived(this, message);
                        }
                    });
        }
    }

    /**
     * Sends a message to the specified recipient observer in the channel.
     *
     * @param message The message to be sent.
     * @param recipient The recipient observer to receive the message.
     */
    public void sendMessage(Message<T> message, ChannelObserver<T> recipient) {
        synchronized (this) {
            for (ChannelObserver<T> observer : observers) {
                if (observer == recipient) {
                    observer.onMessageReceived(this, message);
                    break;
                }
            }
        }
    }

    /**
     * Registers a {@link ChannelObserver} to receive messages from the channel.
     *
     * @param channelObserver The observer to be registered.
     */
    public void register(ChannelObserver<T> channelObserver) {
        synchronized (this) {
            observers.add(channelObserver);
            metrics.incrementObserverCount();
        }
    }

    /**
     * Deregisters a {@link ChannelObserver} from receiving messages from the channel.
     *
     * @param channelObserver The observer to be deregistered.
     */
    public void deregister(ChannelObserver<T> channelObserver) {
        synchronized (this) {
            observers.remove(channelObserver);
            metrics.decrementObserverCount();
        }
    }
}
