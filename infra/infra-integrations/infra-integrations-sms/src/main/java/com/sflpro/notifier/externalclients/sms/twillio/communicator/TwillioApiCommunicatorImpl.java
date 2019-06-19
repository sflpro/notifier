package com.sflpro.notifier.externalclients.sms.twillio.communicator;

import com.sflpro.notifier.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:26 PM
 */
public class TwillioApiCommunicatorImpl implements TwillioApiCommunicator, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TwillioApiCommunicatorImpl.class);

    /* Properties */
    @Value("${twillio.account.authToken}")
    private String accountAuthToken;

    @Value("${twillio.account.sid}")
    private String accountSid;

    private TwilioRestClient twillioRestClient;

    /* Constructors */
    public TwillioApiCommunicatorImpl() {
        super();
    }

    @Override
    public void afterPropertiesSet() {
        /* Create instance of twillio rest client */
        logger.debug("Creating twillio rest client instance with account sid - {} and account authToken - {}", accountSid, accountAuthToken);
        twillioRestClient = new TwilioRestClient.Builder(accountSid, accountAuthToken).build();
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
        } catch (final Exception e) {
            logger.error("Error occurred while sending sms message", e);
            throw new TwillioClientRuntimeException(request.getSenderNumber(), request.getRecipientNumber(), request.getMessageBody(), e);
        }
    }
}
