package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

class RPCQueueMessageHandlerAdapter implements MessageListener {

    private final RPCQueueMessageHandler messageHandler;

    private final RabbitTemplate rabbitTemplate;

    RPCQueueMessageHandlerAdapter(final RPCQueueMessageHandler messageHandler, final RabbitTemplate rabbitTemplate) {
        this.messageHandler = messageHandler;
        this.rabbitTemplate = rabbitTemplate;
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
        final MessageProperties replyMessagProperties = new MessageProperties();
        replyMessagProperties.setContentType("application/json");
        replyMessagProperties.setContentEncoding(StandardCharsets.UTF_8.name());
        replyMessagProperties.setCorrelationId(correlationId);
        rabbitTemplate.send(replyTo, new Message(replyPayload.getBytes(), replyMessagProperties));
    }
}
