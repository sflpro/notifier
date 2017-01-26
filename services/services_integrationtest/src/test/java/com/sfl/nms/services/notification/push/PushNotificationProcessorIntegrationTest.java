package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.notification.model.NotificationState;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.services.notification.model.push.PushNotification;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.test.AbstractServiceIntegrationTest;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        PushNotification pushNotification = getServicesTestHelper().createPushNotification(recipient, getServicesTestHelper().createPushNotificationDto(), getServicesTestHelper().createPushNotificationPropertyDTOs(10));
        assertEquals(NotificationState.CREATED, pushNotification.getState());
        // Process push notification
        pushNotificationProcessingService.processNotification(pushNotification.getId());
        flushAndClear();
        // Reload push notification
        pushNotification = pushNotificationService.getNotificationById(pushNotification.getId());
        assertEquals(NotificationState.SENT, pushNotification.getState());
        assertNotNull(pushNotification.getProviderExternalUuId());
    }
}
