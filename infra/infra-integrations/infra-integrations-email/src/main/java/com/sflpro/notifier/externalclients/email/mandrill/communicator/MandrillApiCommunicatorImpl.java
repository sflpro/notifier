package com.sflpro.notifier.externalclients.email.mandrill.communicator;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillEmailClientRuntimeException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageInvalidException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageRejectedException;
import com.sflpro.notifier.spi.email.EmailMessage;
import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.SpiEmailNotificationFileAttachment;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MandrillApiCommunicatorImpl implements MandrillApiCommunicator {

    /* Logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(MandrillApiCommunicatorImpl.class);

    private static final String MERGE_LANGUAGE_MAILCHIMP = "mailchimp";

    /* Properties */
    private final MandrillMessagesApi mandrillMessagesApi;

    /* Constructors */
    public MandrillApiCommunicatorImpl(final MandrillMessagesApi mandrillMessagesApi) {
        this.mandrillMessagesApi = mandrillMessagesApi;
        LOGGER.debug("Initializing Mandrill web service communicator");
    }

    private static MandrillMessage prepareMessage(final EmailMessage message) {
        // Recipients of the message
        final MandrillMessage.Recipient to = new MandrillMessage.Recipient();
        to.setEmail(message.to());
        to.setType(MandrillMessage.Recipient.Type.TO);
        List<MandrillMessage.Recipient> recipients = new ArrayList<>();
        recipients.add(to);
        // Mandrill message
        final MandrillMessage mandrillMessage = new MandrillMessage();
        mandrillMessage.setTo(recipients);
        mandrillMessage.setFromEmail(message.from());
        mandrillMessage.setMergeLanguage(MERGE_LANGUAGE_MAILCHIMP);
        if (!message.fileAttachments().isEmpty()) {
            mandrillMessage.setAttachments(copyFileAttachments(message.fileAttachments()));
        }
        return mandrillMessage;
    }

    private static MandrillMessage createMandrillMessage(final TemplatedEmailMessage message) {
        // Recipients of the message
        final MandrillMessage mandrillMessage = prepareMessage(message);
        // Merge vars of the message
        final MandrillMessage.MergeVarBucket mergeBucket = new MandrillMessage.MergeVarBucket();
        if (message.variables() != null) {
            mergeBucket.setRcpt(message.to());
            final MandrillMessage.MergeVar[] mergeValues = message.variables().entrySet().stream()
                    .map(mapEntry -> new MandrillMessage.MergeVar(mapEntry.getKey(), mapEntry.getValue()))
                    .toArray(MandrillMessage.MergeVar[]::new);
            mergeBucket.setVars(mergeValues);
        }
        message.subject().ifPresent(mandrillMessage::setSubject);
        mandrillMessage.setMergeVars(Collections.singletonList(mergeBucket));
        return mandrillMessage;
    }

    private static MandrillMessage createMandrillMessage(final SimpleEmailMessage message) {
        // Recipients of the message
        final MandrillMessage mandrillMessage = prepareMessage(message);
        mandrillMessage.setSubject(message.subject());
        mandrillMessage.setHtml(message.body());
        return mandrillMessage;
    }

    /* Interface public methods overrides */
    @Override
    public void sendEmailTemplate(@Nonnull final TemplatedEmailMessage message) {
        assertTemplatedEmailMessageIsValid(message);
        LOGGER.debug("Requested to send email via mandrill, email model - {}", message);
        // Create email send to customer request model
        final MandrillMessage mandrillMessage = createMandrillMessage(message);
        try {
            LOGGER.debug("Performing send email request with parameters - {}", message);
            // Execute request
            final MandrillMessageStatus[] mandrillMessageStatuses = mandrillMessagesApi.sendTemplate(message.templateId(),
                    null, mandrillMessage, false);
            // Handle response
            handleResult(mandrillMessageStatuses, message.templateId(), message.to());
        } catch (final MandrillApiError | IOException e) {
            LOGGER.error("Error occurred while sending sms message", e);
            throw new MandrillEmailClientRuntimeException("MandrillApiError", e);
        }
    }

    @Override
    public void sendEmail(final SimpleEmailMessage message) {
        assertSimpleEmailMessageIsValid(message);
        LOGGER.debug("Requested to send email via mandrill, email model - {}", message);
        // Create email send to customer request model
        final MandrillMessage mandrillMessage = createMandrillMessage(message);
        try {
            LOGGER.debug("Performing send email request with parameters - {}", message);
            // Execute request
            final MandrillMessageStatus[] mandrillMessageStatuses = mandrillMessagesApi.send(mandrillMessage, false);
            // Handle response
            handleResult(mandrillMessageStatuses, null, message.to());
        } catch (final MandrillApiError | IOException ex) {
            LOGGER.error("Error occurred while sending sms message.");
            throw new MandrillEmailClientRuntimeException("MandrillApiError", ex);
        }
    }

    /* Utility methods */
    private static void handleResult(final MandrillMessageStatus[] mandrillMessageStatuses,
                                     @Nullable final String templateId, final String recipientEmail) {
        for (final MandrillMessageStatus mandrillMessageStatus : mandrillMessageStatuses) {
            switch (mandrillMessageStatus.getStatus()) {
                case "rejected":
                    LOGGER.debug("Email '{}' was not sent successfully to '{}', due to '{}' rejection reason.", templateId,
                            recipientEmail, mandrillMessageStatus.getRejectReason());
                    throw new MandrillMessageRejectedException(mandrillMessageStatus);
                case "invalid":
                    LOGGER.debug("Email '{}' was not sent successfully to '{}', since it was considered invalid.", templateId,
                            recipientEmail);
                    throw new MandrillMessageInvalidException(mandrillMessageStatus);
                default:
                    LOGGER.info("Email '{}' was sent successfully to '{}' with '{}' reference number.", mandrillMessageStatus.getEmail(), templateId,
                            mandrillMessageStatus.getId());
                    break;
            }
        }
    }

    private static void assertTemplatedEmailMessageIsValid(final TemplatedEmailMessage message) {
        assertEmailMessageIsValid(message);
        Assert.hasText(message.templateId(), "Mandrill email model template should not be null");
    }

    private static void assertSimpleEmailMessageIsValid(final SimpleEmailMessage message) {
        assertEmailMessageIsValid(message);
        Assert.hasText(message.body(), "Mandrill message body template should not be null");
        Assert.hasText(message.subject(), "Mandrill message subject template should not be null");
    }

    private static void assertEmailMessageIsValid(final EmailMessage message) {
        Assert.notNull(message, "Mandrill email model should not be null");
        Assert.hasText(message.from(), "Mandrill message from should not be null");
        Assert.hasText(message.to(), "Mandrill message to should not be null");
    }

    private static List<MandrillMessage.MessageContent> copyFileAttachments(final List<SpiEmailNotificationFileAttachment> fileAttachmentResource) {
        List<MandrillMessage.MessageContent> destinationAttachments = new ArrayList<>();

        for (SpiEmailNotificationFileAttachment attachment : fileAttachmentResource) {
            MandrillMessage.MessageContent messageContent = new MandrillMessage.MessageContent();
            messageContent.setName(attachment.getFileName());
            messageContent.setType(attachment.getMimeType());
            messageContent.setBinary(true);
            String encodedString = null;
            try {
                byte[] fileContent = FileUtils.readFileToByteArray(new File(attachment.getFileUrl()));
                encodedString = Base64.getEncoder().encodeToString(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageContent.setContent(encodedString);
        }
        return destinationAttachments;
    }
}
