package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.notification.email.template.TemplateEmailService;
import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.fail;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/6/16
 * Time 9:33 PM
 */
public class ApplicationTransactionalEmailServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    final ApplicationTransactionalEmailServiceImpl emailService = new ApplicationTransactionalEmailServiceImpl();

    @Mock
    private TemplateEmailService templateEmailService;

    /* Constructors */
    public ApplicationTransactionalEmailServiceImplTest() {
    }

    /* Test methods */
    @Test
	public void testSendForgotPasswordEmailWithInvalidArguments() {
        // Test data
        final String from = "from@test.com";
        final String to = "to@test.com";
        final NotificationTemplateType notificationTemplateType = NotificationTemplateType.FORGOT_PASSWORD;
        final ResetPasswordEmailTemplateModel templateModel = new ResetPasswordEmailTemplateModel();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailService.sendForgotPasswordEmail(null, to, notificationTemplateType, templateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailService.sendForgotPasswordEmail(from, null, notificationTemplateType, templateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailService.sendForgotPasswordEmail(from, to, null, templateModel);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailService.sendForgotPasswordEmail(from, to, notificationTemplateType, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify all
        verifyAll();
	}

    @Test
	public void testSendForgotPasswordEmail() {
        // Test data
		final EmailNotification notification = new EmailNotification();
        final String from = "from@test.com";
        final String to = "to@test.com";
        final NotificationTemplateType notificationTemplateType = NotificationTemplateType.FORGOT_PASSWORD;
        final ResetPasswordEmailTemplateModel templateModel = new ResetPasswordEmailTemplateModel();
        // Reset
        resetAll();
        // Expectations
        expect(templateEmailService.sendTemplatedEmail(from, to, notificationTemplateType, templateModel)).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        emailService.sendForgotPasswordEmail(from, to, notificationTemplateType, templateModel);
        // Verify all
        verifyAll();
	}
}
