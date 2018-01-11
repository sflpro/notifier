package com.sflpro.notifier.services.notification.impl.email.template;

import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.email.template.model.EmailTemplateModel;
import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.*;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/1/16
 * Time 4:08 PM
 */
public class TemplateEmailServiceImplTest extends AbstractServicesUnitTest {
    /* Test subject and mocks */
    @TestSubject
    private TemplateEmailServiceImpl templateEmailService = new TemplateEmailServiceImpl();

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Mock
    private EmailNotificationService emailNotificationService;

    /* Constructors */
    public TemplateEmailServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testSendTemplatedEmailWithInvalidArguments() {
        // Test data
        final String from = "from@test.com";
        final String to = "to@test.com";
        final NotificationTemplateType notificationTemplateType = NotificationTemplateType.FORGOT_PASSWORD;
        final EmailTemplateModel emailTemplateModel = new ResetPasswordEmailTemplateModel();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            templateEmailService.sendTemplatedEmail(null, to, notificationTemplateType, emailTemplateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            templateEmailService.sendTemplatedEmail(from, null, notificationTemplateType, emailTemplateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            templateEmailService.sendTemplatedEmail(from, to, null, emailTemplateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            templateEmailService.sendTemplatedEmail(from, to, notificationTemplateType, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testSendTemplatedEmail() {
        // Test data
        final NotificationTemplateType notificationTemplateType = NotificationTemplateType.FORGOT_PASSWORD;
        final EmailTemplateModel emailTemplateModel = new ResetPasswordEmailTemplateModel();
        EmailNotificationDto dto = getServicesImplTestHelper().createEmailNotificationDto();
        // This 3 properties not used in tempalte emails.
        dto.setContent(null);
        dto.setSubject(null);
        dto.setClientIpAddress(null);
        final EmailNotification notification = new EmailNotification();
        notification.setId(13L);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.createEmailNotification(dto)).andReturn(notification).once();
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(notification.getId()));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final EmailNotification result = templateEmailService.sendTemplatedEmail(dto.getSenderEmail(), dto.getRecipientEmail(),
                notificationTemplateType, emailTemplateModel);
        assertNotNull(result);
        assertEquals(notification, result);
        // Verify
        verifyAll();
    }
}
