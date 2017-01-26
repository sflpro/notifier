package com.sfl.nms.queue.amqp.rpc;

import com.sfl.nms.queue.amqp.rpc.message.RPCMethodHandler;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:54 PM
 */
public interface RPCServiceAdapter {

    /**
     * Returns methods handlers for adapter
     *
     * @return rpcMethodHandlers
     */
    @Nonnull
    List<RPCMethodHandler<?>> getMethodHandlers();
}
