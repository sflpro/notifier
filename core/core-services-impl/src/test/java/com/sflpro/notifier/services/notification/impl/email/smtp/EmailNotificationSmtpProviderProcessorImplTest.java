package com.sflpro.notifier.services.notification.impl.email.smtp;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.email.MailSendConfiguration;
import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/16/19
 * Time: 8:55 PM
 */
public class EmailNotificationSmtpProviderProcessorImplTest extends AbstractServicesUnitTest {

    @TestSubject
    private EmailNotificationSmtpProviderProcessorImpl emailNotificationSmtpProviderProcessor = new EmailNotificationSmtpProviderProcessorImpl();

    @Mock
    private SmtpTransportService smtpTransportService;

    @Test
    public void testProcessEmailNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationSmtpProviderProcessor.processEmailNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
            emailNotification.setProviderType(NotificationProviderType.MANDRILL);
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
            emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
            emailNotification.setSenderEmail(null);
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessEmailNotification() {
        // Test data
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
        // Reset
        resetAll();
        // Expectations
        smtpTransportService.sendMessageOverSmtp(isA(MailSendConfiguration.class));
        expectLastCall().andAnswer(() -> {
            final MailSendConfiguration configuration = (MailSendConfiguration) getCurrentArguments()[0];
            assertMailSendConfiguration(configuration, emailNotification);
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification);
        assertTrue(result);
        // Verify
        verifyAll();
    }

    private void assertMailSendConfiguration(final MailSendConfiguration configuration, final EmailNotification notification) {
        assertEquals(notification.getSenderEmail(), configuration.getFrom());
        assertEquals(notification.getRecipientEmail(), configuration.getTo());
        assertEquals(notification.getContent(), configuration.getContent());
        assertEquals(notification.getSubject(), configuration.getSubject());
    }
}
