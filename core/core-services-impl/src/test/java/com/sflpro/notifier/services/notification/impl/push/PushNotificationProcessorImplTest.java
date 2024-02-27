package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.services.notification.exception.NotificationInvalidStateException;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.exception.PushNotificationInvalidRouteTokenException;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private PushMessageServiceProvider pushMessageServiceProvider;

    @Mock
    private PushMessageSender pushMessageSender;

    @Mock
    private PushNotificationRecipientService pushNotificationRecipientService;

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
        expect(pushNotificationService.getPushNotificationForProcessing(notificationId)).andReturn(notification).once();
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
        final Exception exceptionDuringSending = new RuntimeException("Exception for testing error flow");
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationService.getPushNotificationForProcessing(eq(notificationId))).andReturn(notification).once();
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(pushMessageServiceProvider.lookupPushMessageSender(notification.getRecipient().getType()))
                .andReturn(Optional.of(pushMessageSender));
        expect(pushMessageSender.send(isA(PushMessage.class))).andThrow(exceptionDuringSending);
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.FAILED)).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        assertThatThrownBy(() -> pushNotificationProcessingService.processNotification(notificationId, Collections.emptyMap()))
                .hasFieldOrPropertyWithValue("cause", exceptionDuringSending);
        // Verify
        verifyAll();
    }

    public void testProcessPushNotificationWhenInvalidTokenExceptionOccursDuringProcessing() {
        // Test data
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        notification.setRecipient(recipient);
        final PushNotificationInvalidRouteTokenException exceptionDuringSending = new PushNotificationInvalidRouteTokenException(
                UUID.randomUUID().toString(), "Exception for testing error flow", null
        );
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationService.getPushNotificationForProcessing(eq(notificationId))).andReturn(notification).once();
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(pushMessageServiceProvider.lookupPushMessageSender(notification.getRecipient().getType()))
                .andReturn(Optional.of(pushMessageSender));
        expect(pushMessageSender.send(isA(PushMessage.class))).andThrow(exceptionDuringSending);
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.FAILED)).andReturn(notification).once();
        expect(pushNotificationRecipientService.updatePushNotificationRecipientStatus(notification.getRecipient().getId(), PushNotificationRecipientStatus.DISABLED));
        // Replay
        replayAll();
        // Run test scenario
        pushNotificationProcessingService.processNotification(notificationId, Collections.emptyMap());
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
        expect(pushNotificationService.getPushNotificationForProcessing(eq(notificationId))).andReturn(notification).once();
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(pushMessageServiceProvider.lookupPushMessageSender(notification.getRecipient().getType()))
                .andReturn(Optional.of(pushMessageSender));
        expect(pushMessageSender.send(isA(PushMessage.class))).andReturn(PushMessageSendingResult.of(pushNotificationExternalUuId));
        expect(pushNotificationService.updateProviderExternalUuid(notificationId, pushNotificationExternalUuId)).andReturn(notification).once();
        expect(pushNotificationService.updateNotificationState(notificationId, NotificationState.SENT)).andReturn(notification).once();
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
