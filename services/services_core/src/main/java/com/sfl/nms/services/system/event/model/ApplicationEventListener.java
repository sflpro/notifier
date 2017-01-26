package com.sfl.nms.services.system.event.model;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 10:06 PM
 */
public interface ApplicationEventListener {

    /**
     * Returns if event listener is interested in the event
     *
     * @param applicationEvent
     * @return subscribed
     */
    boolean subscribed(@Nonnull final ApplicationEvent applicationEvent);

    /**
     * Processes provided event
     *
     * @param applicationEvent
     */
    void process(@Nonnull final ApplicationEvent applicationEvent);

}
