package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.component.UserNotificationComponent;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:32 PM
 */
public class EmailNotificationServiceImplTest extends AbstractNotificationServiceImplTest<EmailNotification> {

    /* Test subject and mocks */
    @TestSubject
    private EmailNotificationServiceImpl emailNotificationService = new EmailNotificationServiceImpl();

    @Mock
    private EmailNotificationRepository emailNotificationRepository;

    @Mock
    private UserNotificationComponent userNotificationComponent;

    /* Constructors */
    public EmailNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreateEmailNotificationWithInvalidArguments() {
        // Test data
        final EmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createEmailNotificationDto();
        final List<EmailNotificationPropertyDto> properties = getServicesImplTestHelper().createEmailNotificationPropertyDtos(2);
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationService.createEmailNotification(null, properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotificationService.createEmailNotification(new EmailNotificationDto(null, emailNotificationDto.getSenderEmail(), emailNotificationDto.getProviderType(), emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress(), emailNotificationDto.getTemplateName()), properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotificationService.createEmailNotification(new EmailNotificationDto(emailNotificationDto.getRecipientEmail(), emailNotificationDto.getSenderEmail(), null, emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress(), emailNotificationDto.getTemplateName()), properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }

        try {
            emailNotificationService.createEmailNotification(emailNotificationDto, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testCreateEmailNotification() {
        // Test data
        final EmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createEmailNotificationDto();
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationRepository.save(isA(EmailNotification.class))).andReturn(emailNotification);
        userNotificationComponent.associateUserWithNotification(emailNotificationDto.getUserUuid(), emailNotification);
        // Replay
        replayAll();
        // Run test scenario
        final EmailNotification result = emailNotificationService.createEmailNotification(emailNotificationDto, Collections.emptyList());
        getServicesImplTestHelper().assertEmailNotification(result, emailNotificationDto);
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationRepository<EmailNotification> getRepository() {
        return emailNotificationRepository;
    }

    @Override
    protected AbstractNotificationServiceImpl<EmailNotification> getService() {
        return emailNotificationService;
    }

    @Override
    protected Class<EmailNotification> getInstanceClass() {
        return EmailNotification.class;
    }

    @Override
    protected EmailNotification getInstance() {
        return getServicesImplTestHelper().createEmailNotification();
    }

}
