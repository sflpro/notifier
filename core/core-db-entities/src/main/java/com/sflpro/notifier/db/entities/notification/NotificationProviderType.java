package com.sflpro.notifier.db.entities.notification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 1:09 PM
 */
public enum NotificationProviderType {
    TWILLIO(NotificationType.SMS), SMTP_SERVER((NotificationType.EMAIL)), AMAZON_SNS(NotificationType.PUSH),
    GOOGLE_GCM(NotificationType.PUSH), APPLE_APNS(NotificationType.PUSH), MANDRILL(NotificationType.EMAIL);

    /* Properties */
    private final List<NotificationType> supportedNotificationTypes;

    NotificationProviderType(final NotificationType... notificationTypes) {
        this.supportedNotificationTypes = Collections.unmodifiableList(Arrays.asList(notificationTypes));
    }

    /* Properties getters and setters */
    public List<NotificationType> getSupportedNotificationTypes() {
        return supportedNotificationTypes;
    }
}
