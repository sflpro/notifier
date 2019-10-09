package com.sflpro.notifier.externalclients.email.smtp;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 11:02 AM
 */
public class SmtpTransportServiceImpl implements com.sflpro.notifier.externalclients.email.smtp.SmtpTransportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmtpTransportServiceImpl.class);

    /**
     * Default content type
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

    @Value("${smtp.starttlsenabled:true}")
    private boolean startTlsEnabled;

    /* Properties */
    private final SmtpSessionHolder smtpSessionHolder = new SmtpSessionHolder();


    /* Constructors */
    SmtpTransportServiceImpl() {
        LOGGER.debug("Initializing smtp transporter service");
    }

    @Override
    public void sendMessageOverSmtp(final String from, final String to, final String subject, final String body) {
        Assert.hasText(from, "from should not be null or empty.");
        Assert.hasText(to, "to should not be null or empty.");
        Assert.hasText(subject, "subject should not be null or empty.");
        Assert.hasText(body, "body should not be null or empty.");
        try {
            /* Create and configure email message */
            final MimeMessage message = new MimeMessage(getSmtpSession());
            message.setSubject(subject);
            message.setFrom(from);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setContent(body, MAIL_CONTENT_TYPE);
            /* Transport message over smtp */
            Transport.send(message);
        } catch (final MessagingException ex) {
            LOGGER.error("Unable to send email from {} to {} with subject {} with body {}", from, to, subject, body);
            throw new com.sflpro.notifier.externalclients.email.smtp.SmtpTransportException(smtpHost, smtpUsername, ex);
        }
    }

    private Session getSmtpSession() {
        try {
            return smtpSessionHolder.get();
        } catch (final ConcurrentException ex) {
            throw new IllegalStateException("Unable to initialize smtp session", ex);
        }
    }

    /* Inner classes */
    private final class SmtpSessionHolder extends LazyInitializer<Session> {

        @Override
        protected Session initialize() {
            final Properties properties = System.getProperties();
            properties.put(PROPERTY_KEY_STARTTLS_ENABLE, startTlsEnabled);
            properties.put(PROPERTY_KEY_HOST, smtpHost);
            properties.put(PROPERTY_KEY_PORT, smtpPort);
            /* Set socket read smtpTimeout */
            properties.put(PROPERTY_KEY_TIMEOUT, smtpTimeout);
            /* Create new smtp session with authentication */
            if (smtpUsername != null && smtpPassword != null) {
                properties.put(PROPERTY_KEY_AUTH, "true");
                return Session.getInstance(properties, new UsernamePasswordAuthenticator(smtpUsername, smtpPassword));
            } else {
                properties.put(PROPERTY_KEY_AUTH, "false");
                return Session.getInstance(properties);
            }
        }
    }

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
