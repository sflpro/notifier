package com.sflpro.notifier.queue.amqp.rpc.impl;

import com.sflpro.notifier.queue.amqp.rpc.RPCServiceAdapter;
import com.sflpro.notifier.queue.amqp.rpc.ServiceRPCAdaptersRegistry;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMethodHandler;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:52 PM
 */
@Service
public class ServiceRPCAdaptersRegistryImpl implements ServiceRPCAdaptersRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRPCAdaptersRegistryImpl.class);

    /* Dependencies */
    private final List<RPCServiceAdapter> adapters;

    private Map<String, RPCMethodHandler> handlersMap;

    /* Constructors */
    @Autowired
    public ServiceRPCAdaptersRegistryImpl(final List<RPCServiceAdapter> adapters) {
        this.adapters = Collections.unmodifiableList(adapters);
        this.handlersMap = new HashMap<>();
    }

    @Override
    public RPCMethodHandler<?> getRpcMethodHandlerForIdentifier(@Nonnull final String identifier) {
        Assert.notNull(identifier, "identifier cannot be null");
        final RPCMethodHandler methodHandler = this.handlersMap.get(identifier);
        if (methodHandler != null) {
            LOGGER.debug("Successfully retrieved method handler for identifier - {}", identifier);
        } else {
            LOGGER.warn("No method handler was found in registry for identifier - {}", identifier);
        }
        return methodHandler;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        LOGGER.debug("{} adapters were registered", adapters.size());
        LOGGER.debug("Building handlers mapping");
        final Map<String, RPCMethodHandler> tempHandlersMap = new HashMap<>();
        // Looping through all adapters
        for (final RPCServiceAdapter adapter : adapters) {
            LOGGER.debug("Registering method handlers for adapter - {}", adapter);
            for (final RPCMethodHandler<?> methodHandler : adapter.getMethodHandlers()) {
                if (tempHandlersMap.containsKey(methodHandler.getMethodIdentifier())) {
                    final RPCMethodHandler existingHandlerForKey = tempHandlersMap.get(methodHandler.getMethodIdentifier());
                    final String message = "There is already method handler registered for identifier - " + methodHandler.getMethodIdentifier() + ", registered handler - " + existingHandlerForKey + " , new handler - " + methodHandler;
                    LOGGER.error(message);
                    throw new ServicesRuntimeException(message);
                }
                tempHandlersMap.put(methodHandler.getMethodIdentifier(), methodHandler);
            }
        }
        // Publish unmodifiable map of handlers
        this.handlersMap = Collections.unmodifiableMap(tempHandlersMap);
        LOGGER.debug("Total {} RPC method handlers were registered", this.handlersMap.size());
    }

    /* Properties getters and setters */
    public List<RPCServiceAdapter> getAdapters() {
        return adapters;
    }
}

