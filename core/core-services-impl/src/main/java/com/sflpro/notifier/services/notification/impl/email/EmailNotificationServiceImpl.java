package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.persistence.repositories.notification.email.EmailNotificationRepository;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;
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
public class EmailNotificationServiceImpl extends AbstractNotificationServiceImpl<EmailNotification> implements EmailNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    /* Constructors */
    public EmailNotificationServiceImpl() {
        LOGGER.debug("Initializing email notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public EmailNotification createEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto) {
        assertEmailNotificationDto(emailNotificationDto);
        LOGGER.debug("Creating email notification for DTO - {}", emailNotificationDto);
        EmailNotification emailNotification = new EmailNotification(true);
        emailNotificationDto.updateDomainEntityProperties(emailNotification);
        // Persist notification
        emailNotification = emailNotificationRepository.save(emailNotification);
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
        Assert.notNull(notificationDto.getSenderEmail(), "Sender email in notification DTO should not be null");
    }

    /* Properties getters and setters */
    public EmailNotificationRepository getEmailNotificationRepository() {
        return emailNotificationRepository;
    }

    public void setEmailNotificationRepository(final EmailNotificationRepository emailNotificationRepository) {
        this.emailNotificationRepository = emailNotificationRepository;
    }
}
