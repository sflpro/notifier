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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

/**
 * Company: SFL LLC
 * Created on 07/02/2018
 *
 * @author Davit Harutyunyan
 */
public class KafkaConnectorServiceImpl implements AmqpConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConnectorServiceImpl.class);

    private final String kafkaTopics;

    @Qualifier(value = "amqpObjectMapper")
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaConnectorServiceImpl(final String kafkaTopics,
                                     final ObjectMapper objectMapper,
                                     final KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTopics = kafkaTopics;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T extends AbstractRPCTransferModel> void publishMessage(
            @Nonnull RPCCallType callType,
            @Nonnull AbstractRPCTransferModel requestModel,
            @Nonnull NotificationSendingPriority sendingPriority,
            @Nonnull Class<T> responseModelClass,
            @Nonnull AmqpResponseHandler<T> responseHandler
    ) {
        Assert.notNull(callType, "Call type should not be null");
        Assert.notNull(requestModel, "Request model should not be null");
        Assert.notNull(responseModelClass, "Response model class should not be null");
        Assert.notNull(responseHandler, "Response handler should not be null");
        try {
            if(!NotificationSendingPriority.MEDIUM.equals(sendingPriority)) {
                LOGGER.warn("Notification sending priorities not supported for kafka but non-default priority({}) received", sendingPriority);
            }
            final RPCMessage rpcMessage = new RPCMessage();
            rpcMessage.setCallIdentifier(callType.getCallIdentifier());
            rpcMessage.setPayload(objectMapper.writeValueAsString(requestModel));
            // Serialize into JSON
            final String jsonMessage = objectMapper.writeValueAsString(rpcMessage);
            kafkaTemplate.send(kafkaTopics, jsonMessage.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception ex) {
            // Only log
            LOGGER.error("Error occurred while executing RPC call, call type - {}, request model - {}", callType, requestModel, ex);
        }
    }
}
