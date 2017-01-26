package com.sfl.nms.queue.producer.connector.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfl.nms.queue.amqp.model.AbstractRPCTransferModel;
import com.sfl.nms.queue.amqp.rpc.RPCCallType;
import com.sfl.nms.queue.amqp.rpc.message.RPCMessage;
import com.sfl.nms.queue.producer.connector.AmqpResponseHandler;
import com.sfl.nms.queue.producer.connector.AmqpConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 11:44 PM
 */
@Service
public class AmqpConnectorServiceImpl implements AmqpConnectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpConnectorServiceImpl.class);

    /* Constants */
    private static final int THREAD_POOL_SIZE = 10;

    /* Dependencies */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier(value = "amqpProducerJsonObjectMapper")
    private ObjectMapper objectMapper;

    /* Properties */
    private ExecutorService executorService;

    /* Constructors */
    public AmqpConnectorServiceImpl() {
        LOGGER.debug("Initializing AMQP connector service");
        initExecutorService();
    }

    @Override
    public <T extends AbstractRPCTransferModel> void publishMessage(@Nonnull final RPCCallType callType, @Nonnull final AbstractRPCTransferModel requestModel, @Nonnull final Class<T> responseModelClass, @Nonnull final AmqpResponseHandler<T> responseHandler) {
        Assert.notNull(callType, "Call type should not be null");
        Assert.notNull(requestModel, "Request model should not be null");
        Assert.notNull(responseModelClass, "Response model class should not be null");
        Assert.notNull(responseHandler, "Response handler should not be null");
        executorService.submit(new MessageSenderTask<T>(callType, requestModel, responseModelClass, responseHandler));

    }

    /* Utility methods */
    private void initExecutorService() {
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /* Inner class */
    private class MessageSenderTask<T extends AbstractRPCTransferModel> implements Runnable {

        /* Properties */
        private final RPCCallType callType;

        private final AbstractRPCTransferModel requestModel;

        private final Class<T> responseModelClass;

        private final AmqpResponseHandler<T> responseHandler;

        /* Constructors */
        public MessageSenderTask(final RPCCallType callType, final AbstractRPCTransferModel requestModel, final Class<T> responseModelClass, final AmqpResponseHandler<T> responseHandler) {
            this.callType = callType;
            this.requestModel = requestModel;
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
                final Message amqpMessage = new Message(jsonMessage.getBytes("UTF8"), new MessageProperties());
                final Message responseMessage = rabbitTemplate.sendAndReceive(amqpMessage);
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
