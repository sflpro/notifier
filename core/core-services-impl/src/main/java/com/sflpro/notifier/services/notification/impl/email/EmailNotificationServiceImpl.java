package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:07 PM
 */
@Service
class EmailNotificationServiceImpl extends AbstractNotificationServiceImpl<EmailNotification> implements EmailNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    /* Constructors */
    EmailNotificationServiceImpl() {
        LOGGER.debug("Initializing email notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public EmailNotification createEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto) {
        assertEmailNotificationDto(emailNotificationDto);
        LOGGER.debug("Creating email notification for DTO - {} and property dtos - {}", emailNotificationDto, emailNotificationDto.getProperties());
        EmailNotification emailNotification = new EmailNotification(true);
        emailNotificationDto.updateDomainEntityProperties(emailNotification);
        // Persist notification
        emailNotification = emailNotificationRepository.save(emailNotification);
        associateNotificationWithUser(emailNotificationDto, emailNotification);
        LOGGER.debug("Successfully created email notification with id - {}, email notification - {}", emailNotification.getId(), emailNotification);
        return emailNotification;
    }

    @Override
    public EmailNotification getEmailNotificationForProcessing(final Long notificationId) {
        Assert.notNull(notificationId, "Null was passed as an argument for parameter 'notificationId'.");
        return emailNotificationRepository.findByIdForProcessingFlow(notificationId);
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


    private void associateNotificationWithUser(@Nonnull final EmailNotificationDto emailNotificationDto, final EmailNotification emailNotification) {
        if (emailNotificationDto.getUserUuid() != null) {
            final User user = userService.getOrCreateUserForUuId(emailNotificationDto.getUserUuid());
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), emailNotification.getId(), new UserNotificationDto());
            LOGGER.debug("Created user notification - {} for user - {} and notification - {}", userNotification, user, emailNotification);
        }
    }

    /* Properties getters and setters */
    public void setEmailNotificationRepository(final EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }
}
