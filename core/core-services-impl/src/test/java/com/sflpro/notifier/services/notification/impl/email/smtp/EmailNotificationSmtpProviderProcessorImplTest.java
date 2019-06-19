package com.sflpro.notifier.services.notification.impl.email.smtp;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.email.MailSendConfiguration;
import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

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

    @Mock
    private TemplatingService templatingService;

    @Test
    public void testProcessEmailNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationSmtpProviderProcessor.processEmailNotification(null, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        try {
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotification.setProviderType(NotificationProviderType.MANDRILL);
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
            emailNotification.setSenderEmail(null);
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
//            final EmailNotification emailNotification = getServicesImplTestHelper().createAndSendEmailNotification();
            emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
            emailNotification.setTemplateName(null);
            emailNotification.setContent(null);
            emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessEmailNotificationWhenNoTemplateProvided() {
        // Test data
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
        emailNotification.setTemplateName(null);
        // Reset
        resetAll();
        // Expectations
        smtpTransportService.sendMessageOverSmtp(isA(MailSendConfiguration.class));
        expectLastCall().andAnswer(() -> {
            final MailSendConfiguration configuration = (MailSendConfiguration) getCurrentArguments()[0];
            assertCommonMailSendConfigurationProperties(configuration, emailNotification);
            assertEquals(emailNotification.getContent(), configuration.getContent());
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, Collections.emptyMap());
        assertTrue(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessEmailNotificationWhenTemplateProvided() {
        // Test data
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setProviderType(NotificationProviderType.SMTP_SERVER);
        final String content = UUID.randomUUID().toString();
        // Reset
        resetAll();
        // Expectations
        expect(templatingService.getContentForTemplate(eq(emailNotification.getTemplateName()), isA(Map.class))).andAnswer(() -> {
            final Map<String, String> parameters = (Map<String, String>) getCurrentArguments()[1];
            assertEquals(emailNotification.getProperties().size(), parameters.size());
            emailNotification.getProperties().forEach(prop -> {
                assertTrue(parameters.containsKey(prop.getPropertyKey()));
                assertEquals(prop.getPropertyValue(), parameters.get(prop.getPropertyKey()));
            });
            return content;
        }).once();
        smtpTransportService.sendMessageOverSmtp(isA(MailSendConfiguration.class));
        expectLastCall().andAnswer(() -> {
            final MailSendConfiguration configuration = (MailSendConfiguration) getCurrentArguments()[0];
            assertCommonMailSendConfigurationProperties(configuration, emailNotification);
            assertEquals(content, configuration.getContent());
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = emailNotificationSmtpProviderProcessor.processEmailNotification(emailNotification, Collections.emptyMap());
        assertTrue(result);
        // Verify
        verifyAll();
    }

    private void assertCommonMailSendConfigurationProperties(final MailSendConfiguration configuration, final EmailNotification notification) {
        assertEquals(notification.getSenderEmail(), configuration.getFrom());
        assertEquals(notification.getRecipientEmail(), configuration.getTo());
        assertEquals(notification.getSubject(), configuration.getSubject());
    }
}
