package io.github.surajkumar.concurrency.channel;

public class Message<T> {
    private final T content;
    private final ChannelObserver<T> sender;

    private Message(T content, ChannelObserver<T> sender) {
        this.content = content;
        this.sender = sender;
    }

    public T getContent() {
        return content;
    }

    public ChannelObserver<T> getSender() {
        return sender;
    }

    public static <U> Message<U> createMessage(U content, ChannelObserver<U> sender) {
        return new Message<>(content, sender);
    }
}
