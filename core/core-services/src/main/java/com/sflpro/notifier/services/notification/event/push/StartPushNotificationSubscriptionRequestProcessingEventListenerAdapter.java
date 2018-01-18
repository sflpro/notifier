package com.sflpro.notifier.services.notification.event.push;

import com.sflpro.notifier.services.system.event.model.ApplicationEvent;
import com.sflpro.notifier.services.system.event.model.ApplicationEventListener;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public abstract class StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter implements ApplicationEventListener {

    /* Constructors */
    public StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter() {
    }


    @Override
    public boolean subscribed(@Nonnull final ApplicationEvent applicationEvent) {
        return (applicationEvent instanceof StartPushNotificationSubscriptionRequestProcessingEvent);
    }

    @Override
    public void process(@Nonnull final ApplicationEvent applicationEvent) {
        final StartPushNotificationSubscriptionRequestProcessingEvent event = (StartPushNotificationSubscriptionRequestProcessingEvent) applicationEvent;
        processPushNotificationSubscriptionRequestEvent(event);
    }

    /* Abstract methods */
    protected abstract void processPushNotificationSubscriptionRequestEvent(final StartPushNotificationSubscriptionRequestProcessingEvent event);
}
