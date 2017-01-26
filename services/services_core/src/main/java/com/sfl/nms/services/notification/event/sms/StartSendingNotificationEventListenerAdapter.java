package com.sfl.nms.services.notification.event.sms;

import com.sfl.nms.services.system.event.model.ApplicationEvent;
import com.sfl.nms.services.system.event.model.ApplicationEventListener;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public abstract class StartSendingNotificationEventListenerAdapter implements ApplicationEventListener {

    /* Constructors */
    public StartSendingNotificationEventListenerAdapter() {
    }


    @Override
    public boolean subscribed(@Nonnull final ApplicationEvent applicationEvent) {
        return (applicationEvent instanceof StartSendingNotificationEvent);
    }

    @Override
    public void process(@Nonnull final ApplicationEvent applicationEvent) {
        final StartSendingNotificationEvent event = (StartSendingNotificationEvent) applicationEvent;
        processStartSendingNotificationEvent(event);
    }

    /* Abstract methods */
    protected abstract void processStartSendingNotificationEvent(final StartSendingNotificationEvent event);
}
