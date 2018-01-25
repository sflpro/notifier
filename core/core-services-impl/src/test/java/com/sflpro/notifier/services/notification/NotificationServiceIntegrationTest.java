package com.sflpro.notifier.services.notification;

import com.sflpro.notifier.db.entities.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:37 PM
 */
public class NotificationServiceIntegrationTest extends AbstractNotificationServiceIntegrationTest<Notification> {

    /* Dependencies */
    @Autowired
    private NotificationService notificationService;

    /* Constructors */
    public NotificationServiceIntegrationTest() {
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationService<Notification> getService() {
        return notificationService;
    }

    @Override
    protected Notification getInstance() {
        return getServicesTestHelper().createSmsNotification();
    }

    @Override
    protected Class<Notification> getInstanceClass() {
        return Notification.class;
    }
}
