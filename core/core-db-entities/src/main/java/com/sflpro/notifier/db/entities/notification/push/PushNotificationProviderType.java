package com.sflpro.notifier.db.entities.notification.push;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/11/15
 * Time: 4:09 PM
 */
public enum PushNotificationProviderType {
    SNS(NotificationProviderType.AMAZON_SNS), APNS(NotificationProviderType.APPLE_APNS), GCM(NotificationProviderType.GOOGLE_GCM),
    FCM(NotificationProviderType.FIREBASE_CLOUD_MESSAGING);

    /* Properties */
    private final NotificationProviderType notificationProviderType;

    /* Constructors */
    PushNotificationProviderType(final NotificationProviderType notificationProviderType) {
        this.notificationProviderType = notificationProviderType;
    }

    public static Optional<PushNotificationProviderType> providerTypeFor(final NotificationProviderType notificationProviderType) {
        return Stream.of(PushNotificationProviderType.values())
                .filter(pushNotificationProviderType -> pushNotificationProviderType.getNotificationProviderType().equals(notificationProviderType))
                .findFirst();
    }

    /* Properties getters and setters */
    public NotificationProviderType getNotificationProviderType() {
        return notificationProviderType;
    }
}
