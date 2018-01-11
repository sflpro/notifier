package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.services.notification.NotificationService;
import com.sflpro.notifier.services.notification.email.EmailNotificationProcessor;
import com.sflpro.notifier.services.notification.exception.UnsupportedNotificationTypeException;
import com.sflpro.notifier.services.notification.model.Notification;
import com.sflpro.notifier.services.notification.model.NotificationType;
import com.sflpro.notifier.services.notification.push.PushNotificationProcessor;
import com.sflpro.notifier.services.notification.sms.SmsNotificationProcessor;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:26 PM
 */
public class NotificationProcessingServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private NotificationProcessingServiceImpl notificationProcessingService = new NotificationProcessingServiceImpl();

    @Mock
    private PushNotificationProcessor pushNotificationProcessor;

    @Mock
    private SmsNotificationProcessor smsNotificationProcessor;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailNotificationProcessor emailNotificationProcessor;

    /* Constructors */
    public NotificationProcessingServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            notificationProcessingService.processNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWithEmailNotification() {
        // Test data
        final Long notificationId = 1L;
        final Notification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        // Reset
        resetAll();
        // Expectations
        expect(notificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        emailNotificationProcessor.processNotification(eq(notificationId));
        expectLastCall();
        // Replay
        replayAll();
        // Run test scenario
        notificationProcessingService.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWithSmsNotification() {
        // Test data
        final Long notificationId = 1L;
        final Notification notification = getServicesImplTestHelper().createSmsNotification();
        notification.setId(notificationId);
        // Reset
        resetAll();
        // Expectations
        expect(notificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        smsNotificationProcessor.processNotification(eq(notificationId));
        expectLastCall();
        // Replay
        replayAll();
        // Run test scenario
        notificationProcessingService.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWithPushNotification() {
        // Test data
        final Long notificationId = 1L;
        final Notification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        // Reset
        resetAll();
        // Expectations
        expect(notificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        pushNotificationProcessor.processNotification(eq(notificationId));
        expectLastCall();
        // Replay
        replayAll();
        // Run test scenario
        notificationProcessingService.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertUnsupportedNotificationTypeException(final UnsupportedNotificationTypeException ex, final NotificationType notificationType) {
        Assert.assertEquals(notificationType, ex.getType());
    }

}
