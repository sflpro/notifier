package com.sflpro.notifier.services.notification.model.push;

import com.sflpro.notifier.services.notification.model.NotificationProviderType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/11/15
 * Time: 4:09 PM
 */
public enum PushNotificationProviderType {
    SNS(NotificationProviderType.AMAZON_SNS), APNS(NotificationProviderType.APPLE_APNS), GCM(NotificationProviderType.GOOGLE_GCM);

    /* Properties */
    private final NotificationProviderType notificationProviderType;

    /* Constructors */
    private PushNotificationProviderType(final NotificationProviderType notificationProviderType) {
        this.notificationProviderType = notificationProviderType;
    }

    /* Properties getters and setters */
    public NotificationProviderType getNotificationProviderType() {
        return notificationProviderType;
    }
}
