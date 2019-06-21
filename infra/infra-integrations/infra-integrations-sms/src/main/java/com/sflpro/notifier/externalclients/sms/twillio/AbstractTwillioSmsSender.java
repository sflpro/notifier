package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.sflpro.notifier.sms.SmsMessage;
import com.sflpro.notifier.sms.SmsMessageSendingResult;
import com.sflpro.notifier.sms.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/21/19
 * Time: 12:12 PM
 */
abstract class AbstractTwillioSmsSender<M extends SmsMessage> implements SmsSender<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTwillioSmsSender.class);


    private final TwillioApiCommunicator twillioApiCommunicator;

    AbstractTwillioSmsSender(final TwillioApiCommunicator twillioApiCommunicator) {
        this.twillioApiCommunicator = twillioApiCommunicator;
    }

    @Override
    public SmsMessageSendingResult send(final M message) {
        LOGGER.debug("Sending sms message with request model - {}", message);
        final SendMessageRequest sendMessageRequest = new SendMessageRequest(
                message.sender(),
                message.recipientNumber(),
                bodyFor(message)
        );
        final SendMessageResponse sendMessageResponse = twillioApiCommunicator.sendMessage(sendMessageRequest);
        LOGGER.debug("Successfully sent sms message, response - {}", sendMessageResponse);
        return SmsMessageSendingResult.of(sendMessageResponse.getSid());
    }

    abstract String bodyFor(final M message);
}
