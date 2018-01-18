package com.sflpro.notifier.services.notification.impl.email.template;

import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.email.template.TemplateEmailService;
import com.sflpro.notifier.services.notification.email.template.model.EmailTemplateModel;
import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/13/16
 * Time 5:56 PM
 */
@Service
public class TemplateEmailServiceImpl implements TemplateEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEmailServiceImpl.class);

    /* Dependencies */
    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    /* Constructors */
    public TemplateEmailServiceImpl() {
        LOGGER.debug("Initializing template email service");
    }

    /* Public methods */
    @Override
    @Nonnull
    public EmailNotification sendTemplatedEmail(final String from, final String to, final NotificationTemplateType templateType, final EmailTemplateModel emailTemplateModel) {
        Assert.notNull(from, "From should not be null");
        Assert.notNull(to, "To should not be null");
        Assert.notNull(templateType, "Template type should not be null");
        Assert.notNull(emailTemplateModel, "Email template model should not be null");
        LOGGER.debug("Sending templated email for channel id - {}, from email - {}, to email - {}, template type - {}, email template model - {}",
                from, to, templateType, emailTemplateModel);

        // Create email notification dto
        final EmailNotificationDto dto = new EmailNotificationDto(to, from, NotificationProviderType.SMTP_SERVER,
                null, null, null);
        final EmailNotification notification = emailNotificationService.createEmailNotification(dto);

        //Process email notification
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(notification.getId()));
        return notification;
    }
}
