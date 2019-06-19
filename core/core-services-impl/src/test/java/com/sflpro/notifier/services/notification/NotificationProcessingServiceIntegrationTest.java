package com.sflpro.notifier.services.notification;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:40 PM
 */
public class NotificationProcessingServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private NotificationProcessingService notificationProcessingService;

    @Autowired
    private NotificationService notificationService;

    /* Constructors */
    public NotificationProcessingServiceIntegrationTest() {
    }

    /* Test methods */
    @Ignore
    @Test
    public void testProcessNotificationWithPushNotification() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        // Create recipient
        final PushNotificationRecipient recipient = getServicesTestHelper().createPushNotificationRecipientForIOSDeviceAndRegisterWithAmazonSns(user, iOSDeviceToken);
        flushAndClear();
        // Create push notification
        Notification notification = getServicesTestHelper().createPushNotification(recipient, getServicesTestHelper().createPushNotificationDto(), getServicesTestHelper().createPushNotificationPropertyDTOs(10));
        assertEquals(NotificationState.CREATED, notification.getState());
        // Process push notification
        notificationProcessingService.processNotification(notification.getId(), Collections.emptyMap());
        flushAndClear();
        // Reload push notification
        notification = notificationService.getNotificationById(notification.getId());
        assertSendNotificationWithExternalUuId(notification);

    }

    @Test
    public void testProcessNotificationWithSmsNotification() {
        // Create recipient
        flushAndClear();
        // Create push notification
        Notification notification = getServicesTestHelper().createSmsNotification();
        assertEquals(NotificationState.CREATED, notification.getState());
        // Process push notification
        notificationProcessingService.processNotification(notification.getId(), Collections.emptyMap());
        flushAndClear();
        // Reload push notification
        notification = notificationService.getNotificationById(notification.getId());
        assertSendNotificationWithExternalUuId(notification);

    }

    @Test
    public void testProcessNotificationWithEmailNotification() {
        // Create recipient
        flushAndClear();
        // Create push notification
        Notification notification = getServicesTestHelper().createEmailNotification();
        assertEquals(NotificationState.CREATED, notification.getState());
        final HashMap<String, String> secureProperties = new HashMap<>();
        secureProperties.putIfAbsent("token", UUID.randomUUID().toString());
        // Process push notification
        notificationProcessingService.processNotification(notification.getId(), secureProperties);
        flushAndClear();
        // Reload push notification
        notification = notificationService.getNotificationById(notification.getId());
        assertSendNotification(notification);

    }

    /* Utility methods */
    private void assertSendNotification(final Notification notification) {
        assertEquals(NotificationState.SENT, notification.getState());
    }

    private void assertSendNotificationWithExternalUuId(final Notification notification) {
        assertSendNotification(notification);
        assertNotNull(notification.getProviderExternalUuId());
    }
}
