package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.email.SpiEmailNotificationFileAttachment;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
    public void sendMessageOverSmtp(final String from,
                                    final String to,
                                    final String subject,
                                    final String body,
                                    final List<SpiEmailNotificationFileAttachment> fileAttachments) {
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
            /* Transport message over smtp */
            Transport.send(message);
        } catch (final MessagingException | MalformedURLException ex) {
            LOGGER.error("Unable to send email from {} to {} with subject {} with body {}", from, to, subject, body);
            throw new com.sflpro.notifier.externalclients.email.smtp.SmtpTransportException(smtpHost, smtpUsername, ex);
        }
    }

    /* Private methods */
    private Multipart setEmailContentParts(final String body, final List<SpiEmailNotificationFileAttachment> attachments)
            throws MessagingException, MalformedURLException {
        Multipart multipart = new MimeMultipart();
        BodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, MAIL_CONTENT_TYPE);
        multipart.addBodyPart(textBody);
        if (!attachments.isEmpty()) {
            for (SpiEmailNotificationFileAttachment attachment : attachments) {
                BodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setFileName(String.format("%s%s", attachment.getFileName(), processMimeType(attachment.getMimeType())));
                attachmentPart.setDataHandler(new DataHandler(new URL(attachment.getFileUrl())));
                multipart.addBodyPart(attachmentPart);
            }
        }
        return multipart;
    }

    private String processMimeType(final String mimeType) {
        switch (mimeType) {
            case "application/x-abiword":
                return ".abw";
            case "application/x-freearc":
                return ".arc";
            case "application/vnd.amazon.ebook":
                return ".azw";
            case "application/epub+zip":
                return ".epub";
            case "application/vnd.ms-fontobject":
                return ".eot";
            case "text/calendar":
                return ".ics";
            case "application/x-shockwave-flash":
                return ".swf";
            case "application/vnd.visio":
                return ".vsd";

            // office document types
            case "application/pdf":
                return ".pdf";
            case "text/plain":
                return ".txt";
            case "application/msword":
                return ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return ".docx";
            case "application/vnd.ms-powerpoint":
                return ".ppt";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return ".pptx";
            case "application/vnd.ms-excel":
                return ".xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return ".xlsx";
            case "text/csv":
                return ".csv";
            case "application/vnd.oasis.opendocument.presentation":
                return ".odp";
            case "application/vnd.oasis.opendocument.spreadsheet":
                return ".ods";
            case "application/vnd.oasis.opendocument.text":
                return ".odt";
            case "application/rtf":
                return ".rtf";

            // web types
            case "application/xml":
                return ".xml";
            case "application/json":
                return ".json";
            case "application/ld+json":
                return ".jsonld";
            case "text/html":
                return ".html";
            case "text/css":
                return ".css";
            case "application/x-httpd-php":
                return ".php";
            case "text/javascript":
                return ".js";
            case "application/xhtml+xml":
                return ".xhtml";
            case "application/vnd.mozilla.xul+xml":
                return ".xul";

            // fonts
            case "font/otf":
                return ".otf";
            case "font/ttf":
                return ".ttf";
            case "font/woff":
                return ".woff";
            case "font/woff2":
                return ".woff2";

            // archives
            case "application/java-archive":
                return ".jar";
            case "application/vnd.rar":
                return ".rar";
            case "application/zip":
                return ".zip";
            case "application/x-7z-compressed":
                return ".7z";
            case "application/x-tar":
                return ".tar";
            case "application/gzip":
                return ".gz";
            case "application/x-bzip":
                return ".bz";
            case "application/x-bzip2":
                return ".bz2";

            // multimedia types
            case "application/ogg":
                return ".ogx";

            // image
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/bmp":
                return ".bmp";
            case "image/tiff":
                return ".tiff";
            case "image/gif":
                return ".gif";
            case "image/vnd.microsoft.icon":
                return ".ico";
            case "image/svg+xml":
                return ".svg";
            case "image/webp":
                return ".webp";


            // sound
            case "audio/aac":
                return ".aac";
            case "audio/mpeg":
                return ".mp3";
            case "audio/wav":
                return ".wav";
            case "audio/webm":
                return ".weba";
            case "audio/opus":
                return ".opus";
            case "audio/midi audio/x-midi":
                return ".midi";
            case "audio/ogg":
                return ".oga";

            // video
            case "video/mp4":
                return ".mp4";
            case "video/x-msvideo":
                return ".avi";
            case "video/mp2t":
                return ".ts";
            case "video/ogg":
                return ".ogv";
            case "video/mpeg":
                return ".mpeg";
            case "video/3gpp2":
                return ".3g2";
            case "video/webm":
                return ".webm";

            // system
            case "application/octet-stream":
                return ".bin";
            case "application/x-sh":
                return ".sh";
            case "application/x-csh":
                return ".csh";

            // installers
            case "application/vnd.android.package-archive":
                return ".apk";
            case "application/vnd.apple.installer+xml":
                return ".mpkg";


            default:
                return mimeType;
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
