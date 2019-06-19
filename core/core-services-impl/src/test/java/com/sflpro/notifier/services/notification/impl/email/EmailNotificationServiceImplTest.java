package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
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
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Mock
    private UserService userService;

    @Mock
    private UserNotificationService userNotificationService;

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
            emailNotificationService.createAndSendEmailNotification(null, properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotificationService.createAndSendEmailNotification(new EmailNotificationDto(null, emailNotificationDto.getSenderEmail(), emailNotificationDto.getProviderType(), emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress(), emailNotificationDto.getTemplateName()), properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            emailNotificationService.createAndSendEmailNotification(new EmailNotificationDto(emailNotificationDto.getRecipientEmail(), emailNotificationDto.getSenderEmail(), null, emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress(), emailNotificationDto.getTemplateName()), properties);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }

        try {
            emailNotificationService.createAndSendEmailNotification(emailNotificationDto, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testCreateEmailNotification_WhenUserUuidNull() {
        // Test data
        final EmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createEmailNotificationDto();
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setId(1L);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationRepository.save(isA(EmailNotification.class))).andReturn(emailNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(emailNotification.getId(), emailNotificationDto.getSecureProperties()));
        // Replay
        replayAll();
        // Run test scenario
        final EmailNotification result = emailNotificationService.createAndSendEmailNotification(emailNotificationDto, Collections.emptyList());
        getServicesImplTestHelper().assertEmailNotification(result, emailNotificationDto);
    }

    @Test
    public void testCreateEmailNotification() {
        // Test data
        final String userUuid = UUID.randomUUID().toString();
        final EmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createEmailNotificationDto();
        emailNotificationDto.setUserUuid(userUuid);
        final EmailNotification emailNotification = getServicesImplTestHelper().createEmailNotification();
        emailNotification.setId(1L);
        final User user = getServicesImplTestHelper().createUser();
        final UserNotification userNotification = getServicesImplTestHelper().createUserNotification();
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationRepository.save(isA(EmailNotification.class))).andReturn(emailNotification);
        expect(userService.getOrCreateUserForUuId(userUuid)).andReturn(user);
        expect(userNotificationService.createUserNotification(user.getId(), emailNotification.getId(), new UserNotificationDto())).andReturn(userNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(emailNotification.getId(), emailNotificationDto.getSecureProperties()));
        // Replay
        replayAll();
        // Run test scenario
        final EmailNotification result = emailNotificationService.createAndSendEmailNotification(emailNotificationDto, Collections.emptyList());
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
