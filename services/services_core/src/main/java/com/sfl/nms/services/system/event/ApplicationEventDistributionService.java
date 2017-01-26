package com.sfl.nms.services.system.event;

import com.sfl.nms.services.system.event.model.ApplicationEvent;
import com.sfl.nms.services.system.event.model.ApplicationEventListener;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 10:03 PM
 */
public interface ApplicationEventDistributionService {

    /**
     * Subscribes listener for particular event
     *
     * @param eventListener
     */
    void subscribe(@Nonnull final ApplicationEventListener eventListener);

    /**
     * Publishes event using asynchronous publishing
     *
     * @param applicationEvent
     */
    void publishAsynchronousEvent(@Nonnull final ApplicationEvent applicationEvent);

    /**
     * Publishes event using synchronous publishing
     *
     * @param applicationEvent
     */
    void publishSynchronousEvent(@Nonnull final ApplicationEvent applicationEvent);
}
