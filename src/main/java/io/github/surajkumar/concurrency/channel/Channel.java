package io.github.surajkumar.concurrency.channel;

import io.github.surajkumar.concurrency.metrics.ChannelMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * The Channel class represents a channel that can send and receive messages.
 *
 * @param <T> The type of the message content.
 */
public class Channel<T> {
    private final List<ChannelObserver<T>> observers = new ArrayList<>();
    private final ChannelMetrics metrics = new ChannelMetrics();

    /**
     * Sends a message to all registered observers of the channel.
     *
     * @param message The message to be sent.
     * @param <T>     The type of the message content.
     */
    public void sendMessage(Message<T> message) {
        metrics.incrementSentMessages();
        synchronized (observers) {
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
     * @param message   The message to be sent.
     * @param recipient The recipient observer to receive the message.
     * @param <T>       The type of the message content.
     */
    public void sendMessage(Message<T> message, ChannelObserver<T> recipient) {
        synchronized (observers) {
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
     * @param <T>             The type of the message content.
     */
    public void register(ChannelObserver<T> channelObserver) {
        synchronized (observers) {
            observers.add(channelObserver);
            metrics.incrementObserverCount();
        }
    }

    /**
     * Deregisters a {@link ChannelObserver} from receiving messages from the channel.
     *
     * @param channelObserver The observer to be deregistered.
     * @param <T>             The type of the message content.
     */
    public void deregister(ChannelObserver<T> channelObserver) {
        synchronized (observers) {
            observers.remove(channelObserver);
            metrics.decrementObserverCount();
        }
    }
}
