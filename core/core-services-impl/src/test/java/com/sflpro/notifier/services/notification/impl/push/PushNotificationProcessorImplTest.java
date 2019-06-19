package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.exception.NotificationInvalidStateException;
import com.sflpro.notifier.services.notification.impl.push.sns.PushNotificationSnsProviderProcessorImpl;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 2:58 PM
 */
public class PushNotificationProcessorImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationProcessorImpl pushNotificationProcessingService = new PushNotificationProcessorImpl();

    @Mock
    private PushNotificationService pushNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private PushNotificationSnsProviderProcessorImpl pushNotificationSnsProcessor;

    /* Constructors */
    public PushNotificationProcessorImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationProcessingService.processNotification(null, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            final Long notificationId = 1L;
            pushNotificationProcessingService.processNotification(notificationId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationWithInvalidState() {
        // Test data
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.PROCESSING);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationProcessingService.processNotification(notificationId, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final NotificationInvalidStateException ex) {
            // Expected
            assertNotificationInvalidStateException(ex, notificationId, notification.getState(), new LinkedHashSet<>(Arrays.asList(NotificationState.CREATED, NotificationState.FAILED)));
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationWhenExceptionOccursDuringProcessing() {
        // Test data
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        notification.setRecipient(recipient);
        final String exceptionMessage = "Exception for testing error flow";
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        expect(pushNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        pushNotificationSnsProcessor.processPushNotification(eq(notification));
        expectLastCall().andThrow(new ServicesRuntimeException("Exception for testing error flow")).once();
        expect(pushNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.FAILED))).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationProcessingService.processNotification(notificationId, Collections.emptyMap());
        } catch (final ServicesRuntimeException ex) {
            // Expected
            ex.getCause().getMessage().equals(exceptionMessage);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotification() {
        // Test data
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        notification.setRecipient(recipient);
        final String pushNotificationExternalUuId = "IU*Y_*HKVITRUTXRUTRUFIL";
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        expect(pushNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        expect(pushNotificationSnsProcessor.processPushNotification(eq(notification))).andReturn(pushNotificationExternalUuId).once();
        expect(pushNotificationService.updateProviderExternalUuid(eq(notificationId), eq(pushNotificationExternalUuId))).andReturn(notification).once();
        expect(pushNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.SENT))).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        pushNotificationProcessingService.processNotification(notificationId, Collections.emptyMap());
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertNotificationInvalidStateException(final NotificationInvalidStateException ex, final Long notificationId, final NotificationState notificationState, final Set<NotificationState> expectedStates) {
        assertEquals(notificationId, ex.getNotificationId());
        Assert.assertEquals(notificationState, ex.getNotificationState());
        assertEquals(expectedStates.size(), ex.getExpectedStates().size());
        ex.getExpectedStates().forEach(currentState -> {
            assertTrue(expectedStates.contains(currentState));
        });
    }

}
