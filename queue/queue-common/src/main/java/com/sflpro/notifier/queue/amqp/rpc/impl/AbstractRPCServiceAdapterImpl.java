package com.sflpro.notifier.queue.amqp.rpc.impl;

import com.sflpro.notifier.queue.amqp.rpc.RPCServiceAdapter;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMethodHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/14/14
 * Time: 1:50 AM
 */
public abstract class AbstractRPCServiceAdapterImpl implements RPCServiceAdapter, InitializingBean {

    /* List of method handlers */
    private List<RPCMethodHandler<?>> handlers;

    public AbstractRPCServiceAdapterImpl() {
        this.handlers = new ArrayList<>();
    }

    public List<RPCMethodHandler<?>> getMethodHandlers() {
        return Collections.unmodifiableList(handlers);
    }

    protected void addMethodHandler(final RPCMethodHandler<?> rpcHandler) {
        Assert.notNull(rpcHandler);
        this.handlers.add(rpcHandler);
    }

    @Override
    public void afterPropertiesSet() {
    }


    /* Abstract methods */

}
