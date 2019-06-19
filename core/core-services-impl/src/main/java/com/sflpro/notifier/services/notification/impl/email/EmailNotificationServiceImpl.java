package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:07 PM
 */
@Service
public class EmailNotificationServiceImpl extends AbstractNotificationServiceImpl<EmailNotification> implements EmailNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    /* Constructors */
    public EmailNotificationServiceImpl() {
        LOGGER.debug("Initializing email notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public EmailNotification createAndSendEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto, @Nonnull final List<EmailNotificationPropertyDto> emailNotificationPropertyDtos) {
        assertEmailNotificationDto(emailNotificationDto);
        Assert.notNull(emailNotificationPropertyDtos, "emailNotificationPropertyDtos should not be null");
        LOGGER.debug("Creating email notification for DTO - {} and property dtos - {}", emailNotificationDto, emailNotificationPropertyDtos);
        EmailNotification emailNotification = new EmailNotification(true);
        emailNotificationDto.updateDomainEntityProperties(emailNotification);
        createAndAddEmailNotificationProperties(emailNotification, emailNotificationPropertyDtos);
        // Persist notification
        emailNotification = emailNotificationRepository.save(emailNotification);
        associateNotificationWithUser(emailNotificationDto, emailNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(emailNotification.getId(), emailNotificationDto.getSecureProperties()));
        LOGGER.debug("Successfully created email notification with id - {}, email notification - {}", emailNotification.getId(), emailNotification);
        return emailNotification;
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationRepository<EmailNotification> getRepository() {
        return emailNotificationRepository;
    }

    @Override
    protected Class<EmailNotification> getInstanceClass() {
        return EmailNotification.class;
    }

    private void assertEmailNotificationDto(final EmailNotificationDto notificationDto) {
        assertNotificationDto(notificationDto);
        Assert.notNull(notificationDto.getProviderType(), "ProviderType in notification DTO should not be null");
        Assert.notNull(notificationDto.getRecipientEmail(), "Recipient email in notification DTO should not be null");
    }

    private static void createAndAddEmailNotificationProperties(final EmailNotification emailNotification, final List<EmailNotificationPropertyDto> emailNotificationPropertyDtos) {
        emailNotificationPropertyDtos.forEach(emailNotificationPropertyDto -> {
            // Assert third party email notification property DTO
            assertEmailNotificationPropertyDto(emailNotificationPropertyDto);
            // Create third party email notification property and set values
            final EmailNotificationProperty emailNotificationProperty = new EmailNotificationProperty();
            // Update properties
            emailNotificationPropertyDto.updateDomainEntityProperties(emailNotificationProperty);
            // Build up relation between property and push notification
            emailNotificationProperty.setEmailNotification(emailNotification);
            emailNotification.getProperties().add(emailNotificationProperty);
        });
    }

    private void associateNotificationWithUser(@Nonnull final EmailNotificationDto emailNotificationDto, final EmailNotification emailNotification) {
        if (emailNotificationDto.getUserUuid() != null) {
            final User user = userService.getOrCreateUserForUuId(emailNotificationDto.getUserUuid());
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), emailNotification.getId(), new UserNotificationDto());
            LOGGER.debug("Created user notification - {} for user - {} and notification - {}", userNotification, user, emailNotification);
        }
    }

    private static void assertEmailNotificationPropertyDto(final EmailNotificationPropertyDto propertyDto) {
        Assert.notNull(propertyDto, "Third party email notification property DTO should not be null");
        Assert.notNull(propertyDto.getPropertyKey(), "Property key in third party email notification property DTO should not be null");
    }

    /* Properties getters and setters */
    public EmailNotificationRepository getEmailNotificationRepository() {
        return emailNotificationRepository;
    }

    public void setEmailNotificationRepository(final EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }
}
