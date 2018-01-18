package com.sflpro.notifier.services.notification.impl.email;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.dto.email.MailSendConfiguration;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

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
    @TestSubject
    private EmailNotificationProcessorImpl emailNotificationProcessor = new EmailNotificationProcessorImpl();

    @Mock
    private SmtpTransportService smtpTransportService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private MandrillMessagesApi mandrillMessagesApi;


    /* Constructors */
    public EmailNotificationProcessorImplTest() {
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
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.SENT))).andReturn(notification).once();
        smtpTransportService.sendMessageOverSmtp(isA(MailSendConfiguration.class));
        expectLastCall().andAnswer(() -> {
            final MailSendConfiguration configuration = (MailSendConfiguration) getCurrentArguments()[0];
            assertMailConfiguration(configuration, notification);
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
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.FAILED))).andReturn(notification).once();
        smtpTransportService.sendMessageOverSmtp(isA(MailSendConfiguration.class));
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
    private void assertMailConfiguration(final MailSendConfiguration configuration, final EmailNotification notification) {
        assertEquals(notification.getSenderEmail(), configuration.getFrom());
        assertEquals(notification.getRecipientEmail(), configuration.getTo());
        Assert.assertEquals(notification.getContent(), configuration.getContent());
        Assert.assertEquals(notification.getSubject(), configuration.getSubject());
    }

}
