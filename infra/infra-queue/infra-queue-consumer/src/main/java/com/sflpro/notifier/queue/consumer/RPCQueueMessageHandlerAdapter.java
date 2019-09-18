package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

class RPCQueueMessageHandlerAdapter implements MessageListener {

    private final RPCQueueMessageHandler messageHandler;

    private final RabbitOperations rabbitOperations;

    RPCQueueMessageHandlerAdapter(final RPCQueueMessageHandler messageHandler, final RabbitOperations rabbitOperations) {
        this.messageHandler = messageHandler;
        this.rabbitOperations = rabbitOperations;
    }

    @Override
    public void onMessage(final Message message) {
        final String replyPayload = messageHandler.handleMessage(message.getBody());
        final String replyTo = message.getMessageProperties().getReplyTo();
        if (StringUtils.isNoneBlank(replyTo)) {
            reply(replyTo, replyPayload, message.getMessageProperties().getCorrelationId());
        }
    }

    private void reply(final String replyTo, final String replyPayload, @Nullable final String correlationId) {
        final MessageProperties replyMessageProperties = new MessageProperties();
        replyMessageProperties.setContentType("application/json");
        replyMessageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        replyMessageProperties.setCorrelationId(correlationId);
        rabbitOperations.send(replyTo, new Message(replyPayload.getBytes(), replyMessageProperties));
    }
}
