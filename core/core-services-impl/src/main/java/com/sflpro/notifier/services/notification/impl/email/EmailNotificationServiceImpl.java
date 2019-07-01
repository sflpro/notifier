package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class EmailNotificationServiceImpl extends AbstractNotificationServiceImpl<EmailNotification> implements EmailNotificationService {
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

    @Value("${email.provider:SMTP_SERVER}")
    private NotificationProviderType providerType = NotificationProviderType.SMTP_SERVER;

    /* Constructors */
    EmailNotificationServiceImpl() {
        LOGGER.debug("Initializing email notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public EmailNotification createAndSendEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto, @Nonnull final List<NotificationPropertyDto> emailNotificationPropertyDtos) {
        assertEmailNotificationDto(emailNotificationDto);
        Assert.notNull(emailNotificationPropertyDtos, "emailNotificationPropertyDtos should not be null");
        LOGGER.debug("Creating email notification for DTO - {} and property dtos - {}", emailNotificationDto, emailNotificationPropertyDtos);
        EmailNotification emailNotification = new EmailNotification(true);
        emailNotificationDto.updateDomainEntityProperties(emailNotification);
        emailNotification.setProviderType(providerType);
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
