package com.sflpro.notifier.api.internal.facade.notification.email.impl;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorType;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.EmailNotificationModel;
import com.sflpro.notifier.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.services.notification.model.UserNotification;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import com.sflpro.notifier.services.user.model.User;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 8:33 PM
 */
public class EmailNotificationServiceFacadeImplTest extends AbstractFacadeUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private EmailNotificationServiceFacadeImpl emailNotificationServiceFacade = new EmailNotificationServiceFacadeImpl();

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private UserService userService;

    @Mock
    private UserNotificationService userNotificationService;

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public EmailNotificationServiceFacadeImplTest() {
    }


    /* Test methods */
    @Test
    public void testCreateEmailNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationServiceFacade.createEmailNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotificationWithValidationErrors() {
        // Test data
        final CreateEmailNotificationRequest request = new CreateEmailNotificationRequest();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreateEmailNotificationResponse> result = emailNotificationServiceFacade.createEmailNotification(request);
        assertNotNull(result);
        assertNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_BODY_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_EMAIL_RECIPIENT_ADDRESS_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_EMAIL_SENDER_ADDRESS_MISSING);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotificationWithoutUser() {
        // Test data
        final CreateEmailNotificationRequest request = getServiceFacadeImplTestHelper().createCreateEmailNotificationRequest();
        request.setUserUuId(null);
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto(request.getRecipientEmail(), request.getSenderEmail(), NotificationProviderType.SMTP_SERVER, request.getBody(), request.getSubject(), request.getClientIpAddress());
        final Long notificationId = 1L;
        final EmailNotification emailNotification = getServiceFacadeImplTestHelper().createEmailNotification();
        emailNotification.setId(notificationId);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.createEmailNotification(eq(emailNotificationDto))).andReturn(emailNotification).once();
        applicationEventDistributionService.publishAsynchronousEvent(eq(new StartSendingNotificationEvent(notificationId)));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreateEmailNotificationResponse> result = emailNotificationServiceFacade.createEmailNotification(request);
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertEquals(0, result.getErrors().size());
        // Assert notification model
        getServiceFacadeImplTestHelper().assertEmailNotificationModel(emailNotification, (EmailNotificationModel) result.getResponse().getNotification());
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotificationWithUser() {
        // Test data
        final CreateEmailNotificationRequest request = getServiceFacadeImplTestHelper().createCreateEmailNotificationRequest();
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto(request.getRecipientEmail(), request.getSenderEmail(), NotificationProviderType.SMTP_SERVER, request.getBody(), request.getSubject(), request.getClientIpAddress());
        final Long notificationId = 1L;
        final EmailNotification emailNotification = getServiceFacadeImplTestHelper().createEmailNotification();
        emailNotification.setId(notificationId);
        final Long userId = 2L;
        final User user = getServiceFacadeImplTestHelper().createUser();
        user.setId(userId);
        final Long userNotificationId = 3L;
        final UserNotification userNotification = getServiceFacadeImplTestHelper().createUserNotification();
        userNotification.setId(userNotificationId);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.createEmailNotification(eq(emailNotificationDto))).andReturn(emailNotification).once();
        expect(userService.getOrCreateUserForUuId(eq(request.getUserUuId()))).andReturn(user).once();
        expect(userNotificationService.createUserNotification(eq(userId), eq(notificationId), eq(new UserNotificationDto()))).andReturn(userNotification).once();
        applicationEventDistributionService.publishAsynchronousEvent(eq(new StartSendingNotificationEvent(notificationId)));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreateEmailNotificationResponse> result = emailNotificationServiceFacade.createEmailNotification(request);
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertEquals(0, result.getErrors().size());
        // Assert notification model
        getServiceFacadeImplTestHelper().assertEmailNotificationModel(emailNotification, (EmailNotificationModel) result.getResponse().getNotification());
        // Verify
        verifyAll();
    }

}
