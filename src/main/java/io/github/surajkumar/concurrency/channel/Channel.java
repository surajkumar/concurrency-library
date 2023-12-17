package io.github.surajkumar.concurrency.channel;

import io.github.surajkumar.concurrency.metrics.ChannelMetrics;

import java.util.ArrayList;
import java.util.List;

public class Channel<T> {
    private final List<ChannelObserver<T>> observers = new ArrayList<>();
    private final ChannelMetrics metrics = new ChannelMetrics();

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

    public void register(ChannelObserver<T> channelObserver) {
        synchronized (observers) {
            observers.add(channelObserver);
            metrics.incrementObserverCount();
        }
    }

    public void deregister(ChannelObserver<T> channelObserver) {
        synchronized (observers) {
            observers.remove(channelObserver);
            metrics.decrementObserverCount();
        }
    }
}
