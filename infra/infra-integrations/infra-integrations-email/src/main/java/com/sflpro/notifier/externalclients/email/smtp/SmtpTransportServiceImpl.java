package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.email.SpiEmailNotificationFileAttachment;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
    public void sendMessageOverSmtp(
            final String from,
            final String to,
            final Set<String> replyTo,
            final String subject,
            final String body,
            final Set<SpiEmailNotificationFileAttachment> fileAttachments
    ) {
        Assert.hasText(from, "from should not be null or empty.");
        Assert.hasText(to, "to should not be null or empty.");
        Assert.hasText(subject, "subject should not be null or empty.");
        Assert.hasText(body, "body should not be null or empty.");
        try {
            /* Create and configure email message */
            final MimeMessage message = new MimeMessage(getSmtpSession());
            message.setContent(setEmailContentParts(body, fileAttachments));
            message.setSubject(subject);
            message.setFrom(from);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            final List<InternetAddress> internetAddresses = new ArrayList<>();
            if (!CollectionUtils.isEmpty(replyTo)) {
                for (final String email : replyTo) {
                    InternetAddress internetAddress = new InternetAddress(email);
                    internetAddresses.add(internetAddress);
                }
                message.setReplyTo(internetAddresses.toArray(new InternetAddress[0]));
            }
            /* Transport message over smtp */
            Transport.send(message);
        } catch (final MessagingException | MalformedURLException ex) {
            LOGGER.error("Unable to send email from {} to {} with subject {} with body {}", from, to, subject, body);
            throw new com.sflpro.notifier.externalclients.email.smtp.SmtpTransportException(smtpHost, smtpUsername, ex);
        }
    }

    /* Private methods */
    private Multipart setEmailContentParts(final String body, final Set<SpiEmailNotificationFileAttachment> attachments)
            throws MessagingException, MalformedURLException {
        Multipart multipart = new MimeMultipart();
        BodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, MAIL_CONTENT_TYPE);
        multipart.addBodyPart(textBody);
        if (!attachments.isEmpty()) {
            for (SpiEmailNotificationFileAttachment attachment : attachments) {
                BodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setFileName(attachment.getFileName());
                attachmentPart.setDataHandler(new DataHandler(new URL(attachment.getFileUrl())));
                multipart.addBodyPart(attachmentPart);
            }
        }
        return multipart;
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
