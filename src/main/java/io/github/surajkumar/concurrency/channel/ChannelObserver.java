package io.github.surajkumar.concurrency.channel;

/**
 * The ChannelObserver interface defines a contract for objects that observe a Channel and receive
 * messages.
 *
 * @param <T> The type of the message content.
 */
public interface ChannelObserver<T> {
    /**
     * The onMessageReceived method is called when a channel receives a message. It is a callback
     * method defined in the ChannelObserver interface. Implementations of this method should handle
     * the received message.
     *
     * @param channel The channel that received the message.
     * @param message The received message.
     */
    void onMessageReceived(Channel<T> channel, Message<T> message);
}
