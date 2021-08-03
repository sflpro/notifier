package com.sflpro.notifier.queue.producer.connector.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;
import com.sflpro.notifier.queue.amqp.model.AbstractRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMessage;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.AmqpResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 11:44 PM
 */
public class RabbitConnectorServiceImpl implements AmqpConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConnectorServiceImpl.class);

    /* Constants */
    private static final int THREAD_POOL_SIZE = 10;
    private static final Map<NotificationSendingPriority, Integer> NOTIFICATION_SENDING_PRIORITIES_MAP;

    /* Dependencies */
    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    /* Properties */
    private final ExecutorService executorService;

    /* Statics */
    static {
        final Map<NotificationSendingPriority, Integer> priorityValues = new HashMap<>();
        priorityValues.put(NotificationSendingPriority.LOW, 0);
        priorityValues.put(NotificationSendingPriority.MEDIUM, 1);
        priorityValues.put(NotificationSendingPriority.HIGH, 2);
        NOTIFICATION_SENDING_PRIORITIES_MAP = Collections.unmodifiableMap(priorityValues);
    }

    /* Constructors */
    public RabbitConnectorServiceImpl(final RabbitTemplate rabbitTemplate, final ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;

        LOGGER.debug("Initializing AMQP connector service");
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @PreDestroy
    private void destroy() {
        this.executorService.shutdown();
    }

    @Override
    public <T extends AbstractRPCTransferModel> void publishMessage(
            @Nonnull final RPCCallType callType,
            @Nonnull final AbstractRPCTransferModel requestModel,
            @Nonnull final NotificationSendingPriority sendingPriority,
            @Nonnull final Class<T> responseModelClass,
            @Nonnull final AmqpResponseHandler<T> responseHandler
    ) {
        Assert.notNull(callType, "Call type should not be null");
        Assert.notNull(requestModel, "Request model should not be null");
        Assert.notNull(sendingPriority, "Sending priority should not be null");
        Assert.notNull(responseModelClass, "Response model class should not be null");
        Assert.notNull(responseHandler, "Response handler should not be null");
        executorService.submit(new MessageSenderTask<T>(callType, requestModel, NOTIFICATION_SENDING_PRIORITIES_MAP.get(sendingPriority), responseModelClass, responseHandler));

    }

    /* Inner class */
    private class MessageSenderTask<T extends AbstractRPCTransferModel> implements Runnable {

        /* Properties */
        private final RPCCallType callType;

        private final AbstractRPCTransferModel requestModel;

        private final Integer priority;

        private final Class<T> responseModelClass;

        private final AmqpResponseHandler<T> responseHandler;

        /* Constructors */
        private MessageSenderTask(final RPCCallType callType, final AbstractRPCTransferModel requestModel, final Integer priority, final Class<T> responseModelClass, final AmqpResponseHandler<T> responseHandler) {
            this.callType = callType;
            this.requestModel = requestModel;
            this.priority = priority;
            this.responseModelClass = responseModelClass;
            this.responseHandler = responseHandler;
        }

        @Override
        public void run() {
            LOGGER.debug("Executing queue RPC request, call type - {}, request model - {}", callType, requestModel);
            try {
                // Create RPC message
                final RPCMessage rpcMessage = new RPCMessage();
                rpcMessage.setCallIdentifier(callType.getCallIdentifier());
                rpcMessage.setPayload(objectMapper.writeValueAsString(requestModel));
                // Serialize into JSON
                final String jsonMessage = objectMapper.writeValueAsString(rpcMessage);
                final Message amqpMessage = new Message(
                        jsonMessage.getBytes(StandardCharsets.UTF_8),
                        MessagePropertiesBuilder.newInstance().setPriority(priority).build()
                );
                final Message responseMessage = rabbitTemplate.sendAndReceive(amqpMessage);
                if (responseMessage == null) {
                    LOGGER.warn("Seems nobody consumes the messages from queue '{}'.", callType.getCallIdentifier());
                    return;
                }
                final T responseModel = objectMapper.readValue(responseMessage.getBody(), responseModelClass);
                LOGGER.debug("Got AMQP response model - {} for request model - {}", responseModel, requestModel);
                // Handle callback
                responseHandler.handleResponse(responseModel);
            } catch (final Exception ex) {
                // Only log
                LOGGER.error("Error occurred while executing RPC call, call type - {}, request model - {}", callType, requestModel, ex);
            }
        }
    }
}
