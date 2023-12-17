package io.github.surajkumar.concurrency.channel;

/**
 * The Message class represents a message object that can be sent through a channel.
 *
 * @param <T> The type of the message content.
 */
public class Message<T> {
    private final T content;
    private final ChannelObserver<T> sender;

    private Message(T content, ChannelObserver<T> sender) {
        this.content = content;
        this.sender = sender;
    }

    /**
     * Retrieves the content of the message.
     *
     * @return The content of the message.
     */
    public T getContent() {
        return content;
    }

    /**
     * Retrieves the sender of the message.
     *
     * @return The sender of the message.
     */
    public ChannelObserver<T> getSender() {
        return sender;
    }

    /**
     * Creates a new message with the given content and sender.
     *
     * @param <U> The type of the message content.
     * @param content The content of the message.
     * @param sender The sender of the message.
     * @return The newly created message.
     */
    public static <U> Message<U> createMessage(U content, ChannelObserver<U> sender) {
        return new Message<>(content, sender);
    }
}
