package com.sflpro.notifier.api.facade.services.email.impl;

import com.sflpro.notifier.api.facade.services.email.EmailNotificationServiceFacade;
import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.apache.commons.lang3.RandomStringUtils;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 2:52 PM
 */
public class EmailNotificationServiceFacadeImpleTest extends AbstractFacadeUnitTest {

    private EmailNotificationServiceFacade emailNotificationServiceFacade;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    private NotificationProviderType providerType = NotificationProviderType.SMTP_SERVER;

    @Before
    public void prepare() {
        emailNotificationServiceFacade = new EmailNotificationServiceFacadeImpl(emailNotificationService, applicationEventDistributionService, providerType);
    }

    @Test
    public void testCreateEmailNotification() {
        final EmailNotification emailNotification = new EmailNotification();
        final CreateEmailNotificationRequest request = getServiceFacadeImplTestHelper().createCreateEmailNotificationRequest();
        emailNotification.setId(1L);
        emailNotification.setProviderType(providerType);
        emailNotification.setRecipientEmail(request.getRecipientEmail());
        emailNotification.setSenderEmail(request.getSenderEmail());
        emailNotification.setReplyToEmails(request.getReplyToEmails());
        emailNotification.setContent(request.getBody());
        emailNotification.setSubject(request.getSubject());
        emailNotification.setFileAttachments(Collections.emptySet());
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto(
                request.getRecipientEmail(),
                request.getSenderEmail(),
                request.getReplyToEmails(),
                request.getBody(),
                request.getSubject(),
                request.getClientIpAddress(),
                request.getTemplateName(),
                providerType
        );
        emailNotificationDto.setUserUuid(request.getUserUuId());
        emailNotificationDto.setProperties(request.getProperties());
        emailNotificationDto.setFileAttachments(Collections.emptySet());
        // Expectations
        expect(emailNotificationService.createEmailNotification(emailNotificationDto)).andReturn(emailNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(emailNotification.getId(), request.getSecureProperties()));
        expectLastCall();
        // Replay
        replayAll();
        // Run test scenario
        assertThat(emailNotificationServiceFacade.createEmailNotification(request).getResponse().getNotification())
                .hasFieldOrPropertyWithValue("senderEmail", request.getSenderEmail())
                .hasFieldOrPropertyWithValue("recipientEmail", request.getRecipientEmail())
                .hasFieldOrPropertyWithValue("replyToEmails", request.getReplyToEmails())
                .hasFieldOrPropertyWithValue("subject", request.getSubject())
                .hasFieldOrPropertyWithValue("body", request.getBody())
                .hasFieldOrPropertyWithValue("state", NotificationStateClientType.CREATED)
                .hasFieldOrPropertyWithValue("type", NotificationClientType.EMAIL);
        verifyAll();
    }

    @Test
    public void testCreateEmailNotificationWithValidationErrors() {
        // Test data
        final CreateEmailNotificationRequest request = getServiceFacadeImplTestHelper().createCreateEmailNotificationRequest();
        request.setRecipientEmail("");
        request.setSenderEmail("");
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreateEmailNotificationResponse> result = emailNotificationServiceFacade.createEmailNotification(request);
        assertNotNull(result);
        assertNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_EMAIL_RECIPIENT_ADDRESS_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_EMAIL_SENDER_ADDRESS_MISSING);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotificationWithPropertyValidationErrors() {
        // Test data
        final CreateEmailNotificationRequest request = getServiceFacadeImplTestHelper().createCreateEmailNotificationRequest();
        final Map<String, String> properties =  new HashMap<>();
        properties.put(RandomStringUtils.randomAlphanumeric(256), RandomStringUtils.randomAlphanumeric(65536));
        request.setProperties(properties);
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreateEmailNotificationResponse> result = emailNotificationServiceFacade.createEmailNotification(request);
        assertNotNull(result);
        assertNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_PROPERTY_KEY_SIZE_VIOLATION);
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_PROPERTY_VALUE_SIZE_VIOLATION);
        // Verify
        verifyAll();
    }
}
