package io.github.surajkumar.concurrency.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class ChannelTest {

    @Test
    void testSendMessageToAllObservers() {
        Channel<String> channel = new Channel<>();
        List<ChannelObserver<String>> observers = new ArrayList<>();
        int totalObservers = 10;

        for (int i = 0; i < totalObservers; i++) {
            ChannelObserver<String> mockObserver = mock(ChannelObserver.class);
            channel.register(mockObserver);
            observers.add(mockObserver);
        }

        Message<String> mockMessage = mock(Message.class);
        channel.sendMessage(mockMessage);

        assertEquals(totalObservers, observers.size());
        observers.forEach(
                o -> {
                    verify(o, times(1)).onMessageReceived(channel, mockMessage);
                });
    }

    @Test
    void testSendMessageFromSpecificObserver() {
        ChannelObserver<String> mockObserver1 = mock(ChannelObserver.class);
        ChannelObserver<String> mockObserver2 = mock(ChannelObserver.class);
        Message<String> mockMessage = mock(Message.class);
        Channel<String> channel = new Channel<>();
        channel.register(mockObserver1);
        channel.register(mockObserver2);
        channel.sendMessage(mockMessage, mockObserver1);
        verify(mockObserver1, times(1)).onMessageReceived(channel, mockMessage);
        verify(mockObserver2, times(0)).onMessageReceived(channel, mockMessage);
    }

    @Test
    void testSendMessageToMultipleThreads() throws InterruptedException {
        class User implements ChannelObserver<String> {
            private final List<String> receivedMessages = new ArrayList<>();

            @Override
            public void onMessageReceived(Channel<String> channel, Message<String> message) {
                receivedMessages.add(message.getContent());
            }
        }

        int numThreads = 5;
        String message = "Hello";

        List<User> userList = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(numThreads);
        Channel<String> channel = new Channel<>();

        for (int i = 0; i < numThreads; i++) {
            User user = new User();
            channel.register(user);
            userList.add(user);
        }

        userList.forEach(
                user -> {
                    channel.sendMessage(Message.createMessage(message, user));
                    latch.countDown();
                });

        latch.await();

        AtomicInteger size = new AtomicInteger();

        userList.forEach(user -> size.addAndGet(user.receivedMessages.size()));

        assertEquals(20, size.get());
    }

    @Test
    void testMessageReceived() throws InterruptedException {
        ChannelObserver<String> observerMock = mock(ChannelObserver.class);
        ChannelObserver<String> senderMock = mock(ChannelObserver.class);

        int numThreads = 5;
        String message = "Hello";

        Channel<String> channel = new Channel<>();
        channel.register(observerMock);

        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(
                            () -> {
                                channel.sendMessage(Message.createMessage(message, senderMock));
                                latch.countDown();
                            })
                    .start();
        }

        latch.await();

        Mockito.verify(observerMock, Mockito.times(numThreads))
                .onMessageReceived(eq(channel), any(Message.class));
    }
}
