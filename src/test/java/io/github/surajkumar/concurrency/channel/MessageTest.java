package io.github.surajkumar.concurrency.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class MessageTest {

    @Test
    void testCreateMessage() {
        String content = "Hello, World!";
        ChannelObserver<String> mock = mock(ChannelObserver.class);
        Message<String> message = Message.createMessage(content, mock);
        assertEquals(content, message.getContent());
        assertEquals(mock, message.getSender()); //world
    }
}
