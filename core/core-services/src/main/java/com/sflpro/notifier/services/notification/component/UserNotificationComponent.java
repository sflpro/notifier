package com.sflpro.notifier.services.notification.component;

import com.sflpro.notifier.db.entities.notification.Notification;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 11:11 AM
 */

public interface UserNotificationComponent {

    void associateUserWithNotification(final String userUuid, final Notification notification);
}
