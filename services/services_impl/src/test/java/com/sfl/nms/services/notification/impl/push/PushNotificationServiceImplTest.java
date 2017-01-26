package com.sfl.nms.services.notification.impl.push;

import com.sfl.nms.services.notification.UserNotificationService;
import com.sfl.nms.services.notification.dto.UserNotificationDto;
import com.sfl.nms.services.notification.dto.push.PushNotificationDto;
import com.sfl.nms.services.notification.dto.push.PushNotificationPropertyDto;
import com.sfl.nms.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sfl.nms.services.notification.impl.AbstractNotificationServiceImpl;
import com.sfl.nms.services.notification.model.UserNotification;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.push.PushNotificationRecipientService;
import com.sfl.nms.services.user.UserService;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.persistence.repositories.notification.AbstractNotificationRepository;
import com.sfl.nms.persistence.repositories.notification.push.PushNotificationRepository;
import com.sfl.nms.services.notification.impl.AbstractNotificationServiceImplTest;
import com.sfl.nms.services.notification.model.push.PushNotification;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipientStatus;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionService;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:55 AM
 */
public class PushNotificationServiceImplTest extends AbstractNotificationServiceImplTest<PushNotification> {

    /* Test subject and mocks */
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

    /* Constructors */
    public PushNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreateNotificationsForUserActiveRecipientsWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final PushNotificationDto pushNotificationDto = getServicesImplTestHelper().createPushNotificationDto();
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationService.createNotificationsForUserActiveRecipients(null, pushNotificationDto, pushNotificationPropertyDTOs);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotificationsForUserActiveRecipients(userId, null, pushNotificationPropertyDTOs);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotificationsForUserActiveRecipients(userId, pushNotificationDto, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotificationsForUserActiveRecipientsWhenNoSubscriptionExistsForUser() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final PushNotificationDto pushNotificationDto = getServicesImplTestHelper().createPushNotificationDto();
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(eq(userId))).andReturn(false).once();
        // Replay
        replayAll();
        // Run test scenario
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForUserActiveRecipients(userId, pushNotificationDto, pushNotificationPropertyDTOs);
        assertNotNull(pushNotifications);
        assertEquals(0, pushNotifications.size());
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateNotificationsForUserActiveRecipients() {
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
        // Expected recipients search parameters
        final PushNotificationRecipientSearchParameters searchParameters = new PushNotificationRecipientSearchParameters();
        searchParameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        searchParameters.setSubscriptionId(subscriptionId);
        // Expected list of recipients
        final List<PushNotificationRecipient> recipients = createPushNotificationsRecipients(10);
        // Create list of push notifications
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = createPushNotificationPropertyDTOs(10);
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
            expect(pushNotificationRecipientService.getPushNotificationRecipientById(eq(recipient.getId()))).andReturn(recipient).once();
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
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForUserActiveRecipients(userId, pushNotificationDto, pushNotificationPropertyDTOs);
        assertNotNull(pushNotifications);
        assertEquals(recipients.size(), pushNotifications.size());
        // Create counter
        counter.setValue(0);
        pushNotifications.forEach(pushNotification -> {
            // Grab push notifications
            final PushNotificationRecipient recipient = recipients.get(counter.intValue());
            getServicesImplTestHelper().assertPushNotification(pushNotification, pushNotificationDto);
            assertEquals(recipient, pushNotification.getRecipient());
            assertEquals(pushNotificationPropertyDTOs.size(), pushNotification.getProperties().size());
            // Create counter
            final MutableInt pushNotificationCounter = new MutableInt(0);
            pushNotification.getProperties().forEach(pushNotificationProperty -> {
                assertEquals(pushNotification, pushNotificationProperty.getPushNotification());
                // Assert property
                final PushNotificationPropertyDto pushNotificationPropertyDto = pushNotificationPropertyDTOs.get(pushNotificationCounter.getValue());
                getServicesImplTestHelper().assertPushNotificationProperty(pushNotificationProperty, pushNotificationPropertyDto);
                // Increment counter
                pushNotificationCounter.increment();
            });
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
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationService.createNotification(null, notificationDto, pushNotificationPropertyDTOs);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotification(recipientId, null, pushNotificationPropertyDTOs);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotification(recipientId, notificationDto, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationService.createNotification(recipientId, new PushNotificationDto(null, notificationDto.getSubject(), notificationDto.getClientIpAddress()), pushNotificationPropertyDTOs);
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
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = createPushNotificationPropertyDTOs(10);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationRecipientService.getPushNotificationRecipientById(eq(recipientId))).andReturn(recipient).once();
        expect(pushNotificationRepository.save(isA(PushNotification.class))).andAnswer(() -> (PushNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotification result = pushNotificationService.createNotification(recipientId, notificationDto, pushNotificationPropertyDTOs);
        getServicesImplTestHelper().assertPushNotification(result, notificationDto);
        assertEquals(recipient, result.getRecipient());
        assertEquals(pushNotificationPropertyDTOs.size(), result.getProperties().size());
        // Create counter
        final MutableInt counter = new MutableInt(0);
        result.getProperties().forEach(pushNotificationProperty -> {
            assertEquals(result, pushNotificationProperty.getPushNotification());
            // Assert property
            final PushNotificationPropertyDto pushNotificationPropertyDto = pushNotificationPropertyDTOs.get(counter.getValue());
            getServicesImplTestHelper().assertPushNotificationProperty(pushNotificationProperty, pushNotificationPropertyDto);
            // Increment counter
            counter.increment();
        });
        // Verify
        verifyAll();
    }

    /* Utility methods */
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

    private List<PushNotificationPropertyDto> createPushNotificationPropertyDTOs(final int count) {
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationPropertyDto pushNotificationPropertyDto = getServicesImplTestHelper().createPushNotificationPropertyDto();
            pushNotificationPropertyDto.setPropertyKey(pushNotificationPropertyDto.getPropertyKey() + "_" + i);
            pushNotificationPropertyDto.setPropertyValue(pushNotificationPropertyDto.getPropertyValue() + "_" + i);
            // Add to the list of notifications
            pushNotificationPropertyDTOs.add(pushNotificationPropertyDto);
        }
        return pushNotificationPropertyDTOs;
    }


}
