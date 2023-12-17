package io.github.surajkumar.concurrency.channel;

public interface ChannelObserver<T> {
    void onMessageReceived(Channel<T> channel, Message<T> message);
}
