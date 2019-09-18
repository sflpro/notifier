package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import com.sflpro.notifier.queue.consumer.test.AbstractQueueConsumerUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

public class RPCQueueMessageHandlerAdapterTest extends AbstractQueueConsumerUnitTest {

    private RPCQueueMessageHandlerAdapter rpcQueueMessageHandlerAdapter;

    @Mock
    private RPCQueueMessageHandler messageHandler;

    @Mock
    private RabbitOperations rabbitOperations;

    @Before
    public void prepare() {
        rpcQueueMessageHandlerAdapter = new RPCQueueMessageHandlerAdapter(messageHandler, rabbitOperations);
    }

    @Test
    public void onMessageWithoutReplyTo() {
        final MessageProperties props = new MessageProperties();
        final byte[] payload = UUID.randomUUID().toString().getBytes();
        final Message message = new Message(payload, props);
        expect(messageHandler.handleMessage(aryEq(payload))).andReturn("{\"id\":1}");
        replayAll();
        rpcQueueMessageHandlerAdapter.onMessage(message);
        verifyAll();
    }

    @Test
    public void onMessageWithReplyTo() {
        final MessageProperties props = new MessageProperties();
        final String replyTo = UUID.randomUUID().toString();
        props.setReplyTo(replyTo);
        final byte[] payload = UUID.randomUUID().toString().getBytes();
        final Message message = new Message(payload, props);
        final String handleMessageResult = "{\"id\":1}";
        expect(messageHandler.handleMessage(aryEq(payload))).andReturn(handleMessageResult);
        rabbitOperations.send(eq(replyTo), isA(Message.class));
        expectLastCall().andAnswer(() -> {
            final Message replyToMessage = (Message) getCurrentArguments()[1];
            assertThat(replyToMessage.getBody()).isEqualTo(handleMessageResult.getBytes());
            return null;
        });
        replayAll();
        rpcQueueMessageHandlerAdapter.onMessage(message);
        verifyAll();
    }
}
