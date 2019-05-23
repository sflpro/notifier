package com.sflpro.notifier.services.notification.impl.email.smtp;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.services.notification.dto.email.MailSendConfiguration;
import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import com.sflpro.notifier.services.template.TemplatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/16/19
 * Time: 8:28 PM
 */
@Component
public class EmailNotificationSmtpProviderProcessorImpl implements EmailNotificationSmtpProviderProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationSmtpProviderProcessorImpl.class);

    @Autowired
    @Qualifier("smtpTransportService")
    private SmtpTransportService smtpTransportService;

    @Autowired
    private TemplatingService templatingService;

    public EmailNotificationSmtpProviderProcessorImpl() {
        LOGGER.debug("Initializing email notification smtp provider processor impl");
    }

    @Override
    public boolean processEmailNotification(@Nonnull final EmailNotification emailNotification, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(emailNotification, "Email notification not null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        Assert.isTrue(NotificationProviderType.SMTP_SERVER.equals(emailNotification.getProviderType()), "Email notification provider type should be SMTP_SERVER");
        Assert.notNull(emailNotification.getSenderEmail(), "Email notification sender for SMTP_SERVER provider should not be null");
        final MailSendConfiguration mailSendConfiguration = createMailSendConfiguration(emailNotification, secureProperties);
        smtpTransportService.sendMessageOverSmtp(mailSendConfiguration);
        LOGGER.debug("Successfully sent email message configuration - {}, notification - {}", mailSendConfiguration, emailNotification);
        return true;
    }

    private MailSendConfiguration createMailSendConfiguration(final EmailNotification emailNotification, final Map<String, String> secureProperties) {
        final MailSendConfiguration mailSendConfiguration = new MailSendConfiguration();
        mailSendConfiguration.setTo(emailNotification.getRecipientEmail());
        mailSendConfiguration.setFrom(emailNotification.getSenderEmail());
        mailSendConfiguration.setSubject(emailNotification.getSubject());
        if(emailNotification.getTemplateName() == null) {
            Assert.notNull(emailNotification.getContent(), "Email content should not be null when template not provided");
            mailSendConfiguration.setContent(emailNotification.getContent());
        } else {
            final Map<String, String> parameters = emailNotification.getProperties()
                    .stream()
                    .collect(Collectors.toMap(EmailNotificationProperty::getPropertyKey, EmailNotificationProperty::getPropertyValue));
            parameters.putAll(secureProperties);
            final String content = templatingService.getContentForTemplate(emailNotification.getTemplateName(), parameters);
            mailSendConfiguration.setContent(content);
        }
        return mailSendConfiguration;
    }
}
