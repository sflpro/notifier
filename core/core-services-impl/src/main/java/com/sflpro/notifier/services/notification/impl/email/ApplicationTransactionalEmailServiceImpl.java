package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.notification.email.ApplicationTransactionalEmailService;
import com.sflpro.notifier.services.notification.email.template.TemplateEmailService;
import com.sflpro.notifier.services.notification.email.template.model.BaseEmailTemplateModel;
import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/2/16
 * Time 12:15 AM
 */
@Service
public class ApplicationTransactionalEmailServiceImpl implements ApplicationTransactionalEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTransactionalEmailServiceImpl.class);

    /* Dependencies */
    @Autowired
    private TemplateEmailService templateEmailService;

    /* Constructors */
    public ApplicationTransactionalEmailServiceImpl() {
        LOGGER.debug("Initializing email service");
    }

    /* Public methods */
    @Override
    @Nonnull
    public EmailNotification sendForgotPasswordEmail(@Nonnull final String from, @Nonnull final String to, @Nonnull final NotificationTemplateType templateType, @Nonnull final ResetPasswordEmailTemplateModel emailTemplateModel) {
        assertFrom(from);
        assertTo(to);
        assertTemplateType(templateType);
        assertEmailTemplateModel(emailTemplateModel);
        LOGGER.debug("Sending forgot password email for channel id - {}, from - {}, to - {}, template type - {}, template model - {}",
                from, to, templateType, emailTemplateModel);
        final EmailNotification notification = templateEmailService.sendTemplatedEmail(from, to, templateType, emailTemplateModel);
        LOGGER.debug("Successfully sent forgot password email for channel id - {}, from - {}, to - {}, template type - {}, template model - {}, notification - {}",
                from, to, templateType, emailTemplateModel, notification);
        return notification;
    }

    /* Utility methods */

    private static void assertFrom(final String from) {
        Assert.notNull(from, "From email should not be null");
    }

    private static void assertTo(final String to) {
        Assert.notNull(to, "To email should not be null");
    }

    private static void assertTemplateType(final NotificationTemplateType notificationTemplateType) {
        Assert.notNull(notificationTemplateType, "Email template type should not be null");
    }

    private static void assertEmailTemplateModel(final BaseEmailTemplateModel templateModel) {
        Assert.notNull(templateModel, "Email template model should not be null");
    }

    /* Properties getters and setters */
    public TemplateEmailService getTemplateEmailService() {
        return templateEmailService;
    }
    
    public void setTemplateEmailService(final TemplateEmailService templateEmailService) {
        this.templateEmailService = templateEmailService;
    }
}
