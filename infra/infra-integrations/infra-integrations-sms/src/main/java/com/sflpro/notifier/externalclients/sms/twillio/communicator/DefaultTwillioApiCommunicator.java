package com.sflpro.notifier.externalclients.sms.twillio.communicator;

import com.sflpro.notifier.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:26 PM
 */
public class DefaultTwillioApiCommunicator implements TwillioApiCommunicator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTwillioApiCommunicator.class);

    private final TwilioRestClient twillioRestClient;

    public DefaultTwillioApiCommunicator(final TwilioRestClient twillioRestClient) {
        this.twillioRestClient = twillioRestClient;
    }

    /* Public methods */
    @Override
    @Nonnull
    public SendMessageResponse sendMessage(@Nonnull final SendMessageRequest request) {
        /* Build request parameters */
        try {
            logger.debug("Preparing to send text message with request - {}", request);
            Message message = Message.creator(new PhoneNumber(request.getRecipientNumber()),
                    new PhoneNumber(request.getSenderNumber()), request.getMessageBody()).create(twillioRestClient);
            /* Create send message response model */
            final SendMessageResponse sendMessageResponse = new SendMessageResponse(message.getSid(), message.getTo(), message.getBody());
            logger.debug("Created response model for send message request - {}", sendMessageResponse);
            return sendMessageResponse;
        } catch (final Exception ex) {
            logger.error("Error occurred while sending sms message via twillio.");
            throw new TwillioClientRuntimeException(request.getSenderNumber(), request.getRecipientNumber(), ex);
        }
    }
}
