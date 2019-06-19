package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.email.SimpleEmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 11:02 AM
 */
class SmtpTransportServiceImpl implements SmtpTransportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmtpTransportServiceImpl.class);

    /**
     * Default body type
     */
    private static final String MAIL_CONTENT_TYPE = "text/html; charset=utf-8";

    /* Property keys */
    private static final String PROPERTY_KEY_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    private static final String PROPERTY_KEY_HOST = "mail.smtp.host";

    private static final String PROPERTY_KEY_PORT = "mail.smtp.port";

    private static final String PROPERTY_KEY_TIMEOUT = "mail.smtp.smtpTimeout";

    private static final String PROPERTY_KEY_AUTH = "mail.smtp.auth";

    /* Configuration */
    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.port}")
    private Integer smtpPort;

    @Value("${smtp.timeout}")
    private Integer smtpTimeout;

    @Value("${smtp.username}")
    private String smtpUsername;

    @Value("${smtp.password}")
    private String smtpPassword;

    /* Properties */
    private final ReentrantLock smtpSessionInitializationLock;

    private volatile Session smtpSession;


    /* Constructors */
    SmtpTransportServiceImpl() {
        this.smtpSessionInitializationLock = new ReentrantLock();
        LOGGER.debug("Initializing smtp transporter service");
    }

    @Override
    public void sendMessageOverSmtp(@Nonnull final SimpleEmailMessage emailMessage) {
        Assert.notNull(emailMessage, "emailMessage should not be null");
        Assert.hasText(emailMessage.subject(), "Message subject should not be empty");
        Assert.hasText(emailMessage.to(), "Recipient email address should not be empty");
        Assert.hasText(emailMessage.from(), "Sender email address should not be empty");
        Assert.hasText(emailMessage.body(), "Message body should not be empty");
        try {
            /* Create and configure email message */
            final MimeMessage message = new MimeMessage(getSmtpSession());
            message.setSubject(emailMessage.subject());
            message.setFrom(emailMessage.from());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailMessage.to()));
            message.setContent(emailMessage.body(), MAIL_CONTENT_TYPE);
            /* Transport message over smtp */
            Transport.send(message);
        } catch (final MessagingException ex) {
            LOGGER.error("Unable to send email: Original stack trace - ", ex);
            throw new SmtpTransportException(smtpHost, smtpUsername, ex);
        }
    }

    /* Utility methods */

    /**
     * Create or return instance of smtp session
     *
     * @return smtpSession
     */
    private Session getSmtpSession() {
        if (smtpSession != null) {
            return this.smtpSession;
        }
        smtpSessionInitializationLock.lock();
        try {
            if (smtpSession != null) {
                return this.smtpSession;
            }
            /* Set properties */
            Properties properties = System.getProperties();
            properties.put(PROPERTY_KEY_STARTTLS_ENABLE, "true");
            properties.put(PROPERTY_KEY_HOST, smtpHost);
            properties.put(PROPERTY_KEY_PORT, smtpPort);
            /* Set socket read smtpTimeout */
            properties.put(PROPERTY_KEY_TIMEOUT, smtpTimeout);
            /* Create new smtp session with authentication */
            if (smtpUsername != null && smtpPassword != null) {
                properties.put(PROPERTY_KEY_AUTH, "true");
                this.smtpSession = Session.getInstance(properties, new UsernamePasswordAuthenticator(smtpUsername, smtpPassword));
            } else {
                properties.put(PROPERTY_KEY_AUTH, "false");
                this.smtpSession = Session.getInstance(properties);
            }
        } finally {
            smtpSessionInitializationLock.unlock();
        }
        return smtpSession;
    }

    /* Inner classes */
    private static final class UsernamePasswordAuthenticator extends Authenticator {

        /* Properties */
        private final String smtpUsername;

        private final String smtpPassword;

        /* Constructors */
        UsernamePasswordAuthenticator(final String smtpUsername, final String smtpPassword) {
            Assert.hasText(smtpUsername, "Smtp username should not be empty");
            Assert.hasText(smtpPassword, "Smtp password should not be empty");
            this.smtpUsername = smtpUsername;
            this.smtpPassword = smtpPassword;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, smtpPassword);
        }
    }
}
