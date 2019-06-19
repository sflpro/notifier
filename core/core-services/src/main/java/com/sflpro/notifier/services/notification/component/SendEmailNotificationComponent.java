package com.sflpro.notifier.services.notification.component;

import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 11:56 AM
 */
public interface SendEmailNotificationComponent {

    void sendEmailNotification(final StartSendingNotificationEvent event);
}
