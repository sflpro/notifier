package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientsParameters;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.user.UserService;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:55 AM
 */
public class PushNotificationServiceImplTest extends AbstractNotificationServiceImplTest<PushNotification> {

    //region Test subject and mocks

    @TestSubject
    private PushNotificationServiceImpl pushNotificationService = new PushNotificationServiceImpl();

    @Mock
    private PushNotificationRepository pushNotificationRepository;

    @Mock
    private PushNotificationRecipientService pushNotificationRecipientService;

    @Mock
    private UserService userService;

    @Mock
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Mock
    private UserNotificationService userNotificationService;

    public PushNotificationServiceImplTest() {
    }

    //endregion

    //region createNotification...

    @Test
    public void testCreateNotificationsForRecipientsWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final PushNotificationDto pushNotificationDto = getServicesImplTestHelper().createPushNotificationDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationService.createNotificationsForRecipients(null, pushNotificationDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotificationsForRecipients(new PushNotificationRecipientsParameters(null), pushNotificationDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotificationsForRecipients(new PushNotificationRecipientsParameters(userId), null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotificationsForRecipientsWhenNoSubscriptionExistsForUser() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final PushNotificationDto pushNotificationDto = getServicesImplTestHelper().createPushNotificationDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(eq(userId))).andReturn(false).once();
        // Replay
        replayAll();
        // Run test scenario
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForRecipients(new PushNotificationRecipientsParameters(userId), pushNotificationDto);
        assertNotNull(pushNotifications);
        assertEquals(0, pushNotifications.size());
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotificationsForRecipients() {
        // Test data
        // Create user
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // Push notification DTO
        final PushNotificationDto pushNotificationDto = getServicesImplTestHelper().createPushNotificationDto();
        // Subscriptions
        final Long subscriptionId = 2L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        // Device
        final String deviceUuId = UUID.randomUUID().toString();
        // Expected recipients search parameters
        final PushNotificationRecipientSearchParameters searchParameters = new PushNotificationRecipientSearchParameters();
        searchParameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        searchParameters.setSubscriptionId(subscriptionId);
        searchParameters.setDeviceUuId(deviceUuId);
        // Expected list of recipients
        final List<PushNotificationRecipient> recipients = createPushNotificationsRecipients(10);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(eq(userId))).andReturn(true).once();
        expect(pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(eq(userId))).andReturn(subscription).once();
        expect(pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(eq(searchParameters), eq(0L), eq(Integer.MAX_VALUE))).andReturn(recipients).once();
        final MutableLong counter = new MutableLong(0);
        recipients.forEach(recipient -> {
            final Long expectedNotificationId = counter.getValue();
            expect(pushNotificationRecipientService.getPushNotificationRecipientById(EasyMock.eq(recipient.getId()))).andReturn(recipient).once();
            expect(pushNotificationRepository.save(isA(PushNotification.class))).andAnswer(() -> {
                final PushNotification pushNotification = (PushNotification) getCurrentArguments()[0];
                pushNotification.setId(expectedNotificationId);
                return pushNotification;
            }).once();
            final UserNotification userNotification = getServicesImplTestHelper().createUserNotification();
            userNotification.setId(expectedNotificationId * 10);
            expect(userNotificationService.createUserNotification(eq(userId), eq(expectedNotificationId), eq(new UserNotificationDto()))).andReturn(userNotification).once();
            counter.increment();
        });
        // Replay
        replayAll();
        // Run test scenario
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForRecipients(new PushNotificationRecipientsParameters(userId, deviceUuId), pushNotificationDto);
        assertNotNull(pushNotifications);
        assertEquals(recipients.size(), pushNotifications.size());
        // Create counter
        counter.setValue(0);
        pushNotifications.forEach(pushNotification -> {
            // Grab push notifications
            final PushNotificationRecipient recipient = recipients.get(counter.intValue());
            getServicesImplTestHelper().assertPushNotification(pushNotification, pushNotificationDto);
            assertEquals(recipient, pushNotification.getRecipient());
            assertEquals(pushNotificationDto.getProperties().size(), pushNotification.getProperties().size());
            assertEquals(pushNotificationDto.getProperties(),
                    pushNotification.getProperties().stream().collect(Collectors.toMap(
                            NotificationProperty::getPropertyKey,
                            NotificationProperty::getPropertyValue
                    ))
            );
            // Increment counter
            counter.increment();
        });
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotificationWithInvalidArguments() {
        // Test data
        final Long recipientId = 1L;
        final PushNotificationDto notificationDto = getServicesImplTestHelper().createPushNotificationDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationService.createNotification(null, notificationDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotification(recipientId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotification() {
        // Test data
        final Long recipientId = 1L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        final PushNotificationDto notificationDto = getServicesImplTestHelper().createPushNotificationDto();
        notificationDto.setProperties(getServicesImplTestHelper().createNotificationProperties(10));
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationRecipientService.getPushNotificationRecipientById(eq(recipientId))).andReturn(recipient).once();
        expect(pushNotificationRepository.save(isA(PushNotification.class))).andAnswer(() -> (PushNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotification result = pushNotificationService.createNotification(recipientId, notificationDto);
        getServicesImplTestHelper().assertPushNotification(result, notificationDto);
        assertEquals(recipient, result.getRecipient());
        assertEquals(notificationDto.getProperties().size(), result.getProperties().size());
        // Create counter
        final MutableInt counter = new MutableInt(0);
        assertEquals(result.getProperties().stream().collect(Collectors.toMap(NotificationProperty::getPropertyKey, NotificationProperty::getPropertyValue)), notificationDto.getProperties());
        // Verify
        verifyAll();
    }

    //endregion

    //region Utility methods

    @Override
    protected AbstractNotificationRepository<PushNotification> getRepository() {
        return pushNotificationRepository;
    }

    @Override
    protected AbstractNotificationServiceImpl<PushNotification> getService() {
        return pushNotificationService;
    }

    @Override
    protected Class<PushNotification> getInstanceClass() {
        return PushNotification.class;
    }

    @Override
    protected PushNotification getInstance() {
        return getServicesImplTestHelper().createPushNotification();
    }

    private List<PushNotificationRecipient> createPushNotificationsRecipients(final int count) {
        final List<PushNotificationRecipient> recipients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
            recipient.setId(Long.valueOf(i));
            recipients.add(recipient);
        }
        return recipients;
    }

    //endregion

}
