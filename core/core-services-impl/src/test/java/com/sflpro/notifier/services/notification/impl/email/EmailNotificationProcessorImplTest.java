package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.email.SimpleEmailMessage;
import com.sflpro.notifier.email.SimpleEmailSender;
import com.sflpro.notifier.email.TemplatedEmailSender;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 2:27 PM
 */
public class EmailNotificationProcessorImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    private EmailNotificationProcessorImpl emailNotificationProcessor;

    @Mock
    private EmailSenderProvider emailSenderProvider;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private SimpleEmailSender simpleEmailSender;

    @Mock
    private TemplatedEmailSender templatedEmailSender;

    @Before
    public void prepare() {
        emailNotificationProcessor = new EmailNotificationProcessorImpl(emailNotificationService, emailSenderProvider, persistenceUtilityService);
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
            emailNotificationProcessor.processNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWithInvalidNotificationState() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.PROCESSING);
        notification.setProviderType(NotificationProviderType.SMTP_SERVER);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(notification)).andReturn(notification);
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationProcessor.processNotification(notificationId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotification() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(notification)).andReturn(notification);
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.SENT))).andReturn(notification).once();
        expect(emailSenderProvider.lookupSimpleEmailSenderFor(notification.getProviderType().name().toLowerCase())).andReturn(Optional.of(simpleEmailSender));
        simpleEmailSender.send(isA(SimpleEmailMessage.class));
        expectLastCall().andAnswer(() -> {
            final SimpleEmailMessage message = (SimpleEmailMessage) getCurrentArguments()[0];
            assertSimpleEmailMessage(message, notification);
            return null;
        }).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        // Run test scenario
        emailNotificationProcessor.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWhenErrorOccursDuringProcessing() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(notification)).andReturn(notification);
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.FAILED))).andReturn(notification).once();
        expect(emailSenderProvider.lookupSimpleEmailSenderFor(notification.getProviderType().name().toLowerCase())).andReturn(Optional.of(simpleEmailSender));
        simpleEmailSender.send(isA(SimpleEmailMessage.class));
        expectLastCall().andThrow(new ServicesRuntimeException("Dummy runtime exception")).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        try {
            // Run test scenario
            emailNotificationProcessor.processNotification(notificationId);
            fail("Exception should be thrown");
        } catch (final ServicesRuntimeException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertSimpleEmailMessage(final SimpleEmailMessage message, final EmailNotification notification) {
        assertEquals(notification.getSenderEmail(), message.from());
        assertEquals(notification.getRecipientEmail(), message.to());
        Assert.assertEquals(notification.getContent(), message.body());
        Assert.assertEquals(notification.getSubject(), message.subject());
    }

}
