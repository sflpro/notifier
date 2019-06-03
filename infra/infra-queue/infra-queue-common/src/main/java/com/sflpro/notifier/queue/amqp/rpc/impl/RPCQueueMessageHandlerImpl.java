package com.sflpro.notifier.queue.amqp.rpc.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import com.sflpro.notifier.queue.amqp.rpc.ServiceRPCAdaptersRegistry;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMessage;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMethodHandler;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:34 PM
 */
@Service
public class RPCQueueMessageHandlerImpl implements RPCQueueMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPCQueueMessageHandlerImpl.class);

    /* Dependencies */
    private final ObjectMapper objectMapper;

    private final ServiceRPCAdaptersRegistry serviceRPCAdaptersRegistry;

    @Autowired
    public RPCQueueMessageHandlerImpl(final ObjectMapper objectMapper, final ServiceRPCAdaptersRegistry serviceRPCAdaptersRegistry) {
        this.objectMapper = objectMapper;
        this.serviceRPCAdaptersRegistry = serviceRPCAdaptersRegistry;
    }

    @Override
    public String handleMessage(final Object message) {
        LOGGER.debug("Message arrived in message handler");
        try {
            // Trace execution time
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // Process request
            final String messageBody = new String((byte[]) message, "UTF8");
            LOGGER.debug("Value of the message - {}", messageBody);
            final RPCMessage rpcMessage = convertToRpcMessage(messageBody);
            final String result = processRpcMessage(rpcMessage);
            // Stop stop watch
            stopWatch.stop();
            LOGGER.debug("Processing of the RPC queue call - {} took - {}", rpcMessage.getCallIdentifier(), stopWatch.getTime());
            return result;
        } catch (Exception ex) {
            LOGGER.error("Error occurred while processing request - ", ex);
        }
        return StringUtils.EMPTY;
    }

    /* Utility methods */
    private String processRpcMessage(final RPCMessage rpcMessage) throws IOException {
        final RPCMethodHandler<?> rpcMethodHandler = serviceRPCAdaptersRegistry.getRpcMethodHandlerForIdentifier(rpcMessage.getCallIdentifier());
        // Deserialize JSON
        final Object rpcParameterValue;
        if (StringUtils.isNotBlank(rpcMessage.getPayload())) {
            rpcParameterValue = objectMapper.readValue(rpcMessage.getPayload(), rpcMethodHandler.getMethodParameterClass());
        } else {
            rpcParameterValue = null;
        }
        final Object executionResult = rpcMethodHandler.executeMethod(rpcParameterValue);
        // Create RPC call result
        final String result = objectMapper.writeValueAsString(executionResult);
        LOGGER.debug("{} result was retrieved for RPC message - {}", result, rpcMessage);
        return result;
    }


    private RPCMessage convertToRpcMessage(final String message) {
        try {
            final RPCMessage rpcMessage = objectMapper.readValue(message, RPCMessage.class);
            LOGGER.debug("Converted message to RPC message - {}", rpcMessage);
            return rpcMessage;
        } catch (Exception ex) {
            final String errorMessage = "Error occurred while converting message - " + message + " to class - " + RPCMessage.class;
            LOGGER.error(errorMessage, ex);
            throw new ServicesRuntimeException(errorMessage, ex);
        }
    }


    /* Getters and setters */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ServiceRPCAdaptersRegistry getServiceRPCAdaptersRegistry() {
        return serviceRPCAdaptersRegistry;
    }
}
