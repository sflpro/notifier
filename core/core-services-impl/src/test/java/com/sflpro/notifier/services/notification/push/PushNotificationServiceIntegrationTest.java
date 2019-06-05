package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.AbstractNotificationServiceIntegrationTest;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 12:11 PM
 */
public class PushNotificationServiceIntegrationTest extends AbstractNotificationServiceIntegrationTest<PushNotification> {

    /* Dependencies */
    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserNotificationService userNotificationService;

    /* Constructors */
    public PushNotificationServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testCreateNotificationsForUserActiveRecipients() {
        // Prepare data
        final User customer = getServicesTestHelper().createUser();
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription(customer, new PushNotificationSubscriptionDto());
        final List<PushNotificationRecipient> recipients = createPushNotificationRecipients(subscription, 10);
        final PushNotificationDto notificationDto = getServicesTestHelper().createPushNotificationDto();
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = getServicesTestHelper().createPushNotificationPropertyDTOs(10);
        flushAndClear();
        // Create push notifications
        final List<PushNotification> notifications = pushNotificationService.createNotificationsForUserActiveRecipients(customer.getId(), notificationDto, pushNotificationPropertyDTOs);
        assertNotNull(notifications);
        assertEquals(recipients.size(), notifications.size());
        // Load user notifications for customer
        final List<UserNotification> userNotifications = userNotificationService.getUserNotificationsByUserId(customer.getId());
        // Assert notifications
        final MutableInt counter = new MutableInt(0);
        notifications.forEach(notification -> {
            final PushNotificationRecipient recipient = recipients.get(counter.intValue());
            assertPushNotification(notification, notificationDto, recipient, pushNotificationPropertyDTOs);
            assertUserNotificationExists(userNotifications, notification.getId());
            counter.increment();
        });
    }

    @Test
    public void testCreateNotification() {
        // Prepare data
        final PushNotificationDto notificationDto = getServicesTestHelper().createPushNotificationDto();
        final PushNotificationRecipient recipient = getServicesTestHelper().createPushNotificationSnsRecipient();
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = getServicesTestHelper().createPushNotificationPropertyDTOs(10);
        flushAndClear();
        // Create push notification
        PushNotification result = pushNotificationService.createNotification(recipient.getId(), notificationDto, pushNotificationPropertyDTOs);
        assertPushNotification(result, notificationDto, recipient, pushNotificationPropertyDTOs);
        // Flush, clear, reload and assert
        flushAndClear();
        result = pushNotificationService.getNotificationById(result.getId());
        assertPushNotification(result, notificationDto, recipient, pushNotificationPropertyDTOs);
    }

    /* Utility methods */
    private void assertUserNotificationExists(final List<UserNotification> userNotifications, final Long notificationId) {
        final MutableBoolean found = new MutableBoolean(Boolean.FALSE);
        userNotifications.forEach(userNotification -> {
            if (userNotification.getNotification().getId().equals(notificationId)) {
                found.setTrue();
            }
        });
        assertTrue(found.getValue());
    }

    private void assertPushNotification(final PushNotification result, final PushNotificationDto notificationDto, final PushNotificationRecipient recipient, final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        getServicesTestHelper().assertPushNotification(result, notificationDto);
        assertNotNull(result.getRecipient());
        Assert.assertEquals(recipient.getId(), result.getRecipient().getId());
        Assert.assertEquals(recipient.getType().getNotificationProviderType(), result.getProviderType());
        assertPushNotificationProperties(pushNotificationPropertyDTOs, result.getProperties(), result);
    }

    private List<PushNotificationRecipient> createPushNotificationRecipients(final PushNotificationSubscription subscription, final int count) {
        final List<PushNotificationRecipient> recipients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // Create recipient DTO
            final PushNotificationSnsRecipientDto snsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
            snsRecipientDto.setDestinationRouteToken(UUID.randomUUID().toString());
            // Create recipient
            final PushNotificationRecipient recipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, snsRecipientDto);
            recipients.add(recipient);
        }
        return recipients;
    }

    private void assertPushNotificationProperties(final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs, final Set<PushNotificationProperty> pushNotificationProperties, final PushNotification pushNotification) {
        assertEquals(pushNotificationPropertyDTOs.size(), pushNotificationProperties.size());
        pushNotificationProperties.forEach(pushNotificationProperty -> {
            final MutableBoolean mutableBoolean = new MutableBoolean(Boolean.FALSE);
            pushNotificationPropertyDTOs.forEach(pushNotificationPropertyDto -> {
                assertNotNull(pushNotificationProperty.getPushNotification());
                Assert.assertEquals(pushNotification.getId(), pushNotificationProperty.getPushNotification().getId());
                if (pushNotificationPropertyDto.getPropertyKey().equals(pushNotificationProperty.getPropertyKey())) {
                    mutableBoolean.setTrue();
                    getServicesTestHelper().assertPushNotificationProperty(pushNotificationProperty, pushNotificationPropertyDto);
                }
            });
            assertTrue(mutableBoolean.getValue());
        });
    }

    @Override
    protected AbstractNotificationService<PushNotification> getService() {
        return pushNotificationService;
    }

    @Override
    protected PushNotification getInstance() {
        return getServicesTestHelper().createPushNotification();
    }

    @Override
    protected Class<PushNotification> getInstanceClass() {
        return PushNotification.class;
    }

}
