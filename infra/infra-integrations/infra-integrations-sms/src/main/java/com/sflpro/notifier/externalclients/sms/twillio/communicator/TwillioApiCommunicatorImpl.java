package com.sflpro.notifier.externalclients.sms.twillio.communicator;

import com.sflpro.notifier.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:26 PM
 */
@Component
public class TwillioApiCommunicatorImpl implements TwillioApiCommunicator, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwillioApiCommunicatorImpl.class);

    /* Constants */
    private static final String REQUEST_PARAM_KEY_RECIPIENT_NUMBER = "To";

    private static final String REQUEST_PARAM_KEY_SENDER_NUMBER = "From";

    private static final String REQUEST_PARAM_KEY_MESSAGE_BODY = "Body";

    /* Properties */
    @Value("#{appProperties['twillio.account.authToken']}")
    private String accountAuthToken;

    @Value("#{appProperties['twillio.account.sid']}")
    private String accountSid;

    private TwilioRestClient twillioRestClient;

    /* Constructors */
    public TwillioApiCommunicatorImpl() {
        super();
    }

    @Override
    public void afterPropertiesSet() {
        /* Create instance of twillio rest client */
        LOGGER.debug("Creating twillio rest client instance with account sid - {} and account authToken - {}", accountSid, accountAuthToken);
        twillioRestClient = new TwilioRestClient(accountSid, accountAuthToken);
    }

    /* Public methods */
    @Override
    @Nonnull
    public SendMessageResponse sendMessage(@Nonnull final SendMessageRequest request) {
        /* Build request parameters */
        final List<NameValuePair> params = createSendMessageRequestParams(request);
        /* Create message factory */
        final MessageFactory messageFactory = getTwillioRestClient().getAccount().getMessageFactory();
        try {
            LOGGER.debug("Performing send message request with parameters - {}", params);
            final Message message = messageFactory.create(params);
            LOGGER.debug("Successfully sent text message with request - {}, response message - {}", request, message);
            /* Create send message response model */
            final SendMessageResponse sendMessageResponse = new SendMessageResponse(message.getSid(), message.getTo(), message.getBody());
            LOGGER.debug("Created response model for send message request - {}", sendMessageResponse);
            return sendMessageResponse;
        } catch (final TwilioRestException e) {
            LOGGER.error("Error occurred while sending sms message", e);
            throw new TwillioClientRuntimeException(request.getSenderNumber(), request.getRecipientNumber(), request.getMessageBody(), e);
        }
    }

    /* Utility methods */
    private List<NameValuePair> createSendMessageRequestParams(final SendMessageRequest request) {
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(REQUEST_PARAM_KEY_SENDER_NUMBER, request.getSenderNumber()));
        params.add(new BasicNameValuePair(REQUEST_PARAM_KEY_RECIPIENT_NUMBER, request.getRecipientNumber()));
        params.add(new BasicNameValuePair(REQUEST_PARAM_KEY_MESSAGE_BODY, request.getMessageBody()));
        return Collections.unmodifiableList(params);
    }

    /* Getters and setters */
    public TwilioRestClient getTwillioRestClient() {
        return twillioRestClient;
    }

    public void setTwillioRestClient(final TwilioRestClient twillioRestClient) {
        this.twillioRestClient = twillioRestClient;
    }

    public String getAccountAuthToken() {
        return accountAuthToken;
    }

    public void setAccountAuthToken(final String accountAuthToken) {
        this.accountAuthToken = accountAuthToken;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(final String accountSid) {
        this.accountSid = accountSid;
    }
}
