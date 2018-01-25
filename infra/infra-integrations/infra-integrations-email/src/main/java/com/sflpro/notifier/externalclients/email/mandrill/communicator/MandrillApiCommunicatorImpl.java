package com.sflpro.notifier.externalclients.email.mandrill.communicator;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillApiDisabledException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillEmailClientRuntimeException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageInvalidException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageRejectedException;
import com.sflpro.notifier.externalclients.email.mandrill.model.request.SendEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
@Component
public class MandrillApiCommunicatorImpl implements MandrillApiCommunicator, InitializingBean {

    /* Logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(MandrillApiCommunicatorImpl.class);

    private static final String MERGE_LANGUAGE_MAILCHIMP = "mailchimp";

    /* Properties */
    @Value("#{appProperties['mandrill.service.token']}")
    private String token;

    private MandrillMessagesApi mandrillMessagesApi;

    /* Constructors */
    public MandrillApiCommunicatorImpl() {
        LOGGER.debug("Initializing Mandrill web service communicator");
    }

    private void chkMandrillApi() {
        if (token == null) {
            throw new MandrillApiDisabledException();
        }
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.debug("Initializing Mandrill Messages API");
        chkMandrillApi();
        this.mandrillMessagesApi = new MandrillMessagesApi(token);
    }

    private MandrillMessage createMandrillMessage(final SendEmailRequest sendEmailRequest) {
        // Recipients of the message
        final MandrillMessage.Recipient to = new MandrillMessage.Recipient();
        to.setEmail(sendEmailRequest.getRecipientMail());
        to.setType(MandrillMessage.Recipient.Type.TO);
        List<MandrillMessage.Recipient> recipients = new ArrayList<>();
        recipients.add(to);

        // Merge vars of the message
        final MandrillMessage.MergeVarBucket mergeBucket = new MandrillMessage.MergeVarBucket();
        if (sendEmailRequest.getTemplateContent() != null) {
            mergeBucket.setRcpt(sendEmailRequest.getRecipientMail());
            final MandrillMessage.MergeVar[] mergeValues = sendEmailRequest.getTemplateContent().entrySet().stream()
                    .map(mapEntry -> new MandrillMessage.MergeVar(mapEntry.getKey().toUpperCase(), mapEntry.getValue()))
                    .toArray(MandrillMessage.MergeVar[]::new);
            mergeBucket.setVars(mergeValues);
        }

        // Mandrill message
        final MandrillMessage mandrillMessage = new MandrillMessage();
        mandrillMessage.setTo(recipients);
        mandrillMessage.setMergeLanguage(MERGE_LANGUAGE_MAILCHIMP);
        mandrillMessage.setMergeVars(Collections.singletonList(mergeBucket));

        return mandrillMessage;
    }

    /* Interface public methods overrides */
    @Override
    public boolean sendEmailTemplate(@Nonnull final SendEmailRequest sendEmailRequest) {
        assertMandrillEmailModel(sendEmailRequest);
        LOGGER.debug("Requested to send email via mandrill, email model - {}", sendEmailRequest);

        chkMandrillApi();

        // Create email send to customer request model
        MandrillMessage mandrillMessage = createMandrillMessage(sendEmailRequest);
        try {
            LOGGER.debug("Performing send email request with parameters - {}", sendEmailRequest);
            // Execute request
            final MandrillMessageStatus[] mandrillMessageStatuses = mandrillMessagesApi.sendTemplate(sendEmailRequest.getTemplateName(),
                    null, mandrillMessage, false);
            // Extract response
            for (MandrillMessageStatus mandrillMessageStatus : mandrillMessageStatuses) {
                switch (mandrillMessageStatus.getStatus()) {
                    case "rejected":
                        LOGGER.debug("Email '{}' was not sent successfully to '{}', due to '{}' rejection reason.", sendEmailRequest.getTemplateName(),
                                sendEmailRequest.getRecipientMail(), mandrillMessageStatus.getRejectReason());

                        throw new MandrillMessageRejectedException(mandrillMessageStatus);
                    case "invalid":
                        LOGGER.debug("Email '{}' was not sent successfully to '{}', since it was considered invalid.", sendEmailRequest.getTemplateName(),
                                sendEmailRequest.getRecipientMail());

                        throw new MandrillMessageInvalidException(mandrillMessageStatus);
                    default:
                        LOGGER.info("Email '{}' was sent successfully to '{}' with '{}' reference number.", sendEmailRequest.getTemplateName(),
                                mandrillMessageStatus.getEmail(), mandrillMessageStatus.getId());
                        break;
                }
            }
            return true;
        } catch (final MandrillApiError | IOException e) {
            LOGGER.error("Error occurred while sending sms message", e);
            throw new MandrillEmailClientRuntimeException("MandrillApiError", e);
        }
    }

    /* Utility methods */
    private static void assertMandrillEmailModel(final SendEmailRequest sendEmailRequest) {
        Assert.notNull(sendEmailRequest, "Mandrill email model should not be null");
        Assert.notNull(sendEmailRequest.getTemplateName(), "Mandrill email model template should not be null");
    }

    /* Dependencies setters */
    public void setToken(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setMandrillMessagesApi(final MandrillMessagesApi mandrillMessagesApi) {
        this.mandrillMessagesApi = mandrillMessagesApi;
    }
}
