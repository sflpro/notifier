package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 3:00 PM
 */
@Ignore
public class PushNotificationProcessorIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationProcessor pushNotificationProcessingService;

    @Autowired
    private PushNotificationService pushNotificationService;

    /* Constructors */
    public PushNotificationProcessorIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotification() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        // Create recipient
        final PushNotificationRecipient recipient = getServicesTestHelper().createPushNotificationRecipientForIOSDeviceAndRegisterWithAmazonSns(user, iOSDeviceToken);
        flushAndClear();
        // Create push notification
        PushNotification pushNotification = getServicesTestHelper().createPushNotification(recipient, getServicesTestHelper().createPushNotificationDto());
        Assert.assertEquals(NotificationState.CREATED, pushNotification.getState());
        // Process push notification
        pushNotificationProcessingService.processNotification(pushNotification.getId(), Collections.emptyMap());
        flushAndClear();
        // Reload push notification
        pushNotification = pushNotificationService.getNotificationById(pushNotification.getId());
        Assert.assertEquals(NotificationState.SENT, pushNotification.getState());
        assertNotNull(pushNotification.getProviderExternalUuId());
    }
}
