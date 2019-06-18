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
 * Date: 6/18/19
 * Time: 4:37 PM
 */
class TwillioSmsSender implements SmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwillioSmsSender.class);

    private final TwillioApiCommunicator twillioApiCommunicator;

    TwillioSmsSender(final TwillioApiCommunicator twillioApiCommunicator) {
        this.twillioApiCommunicator = twillioApiCommunicator;
    }

    @Override
    public SmsMessageSendingResult send(final SmsMessage smsMessage) {
        LOGGER.debug("Sending sms message with request model - {}", smsMessage);
        final SendMessageRequest sendMessageRequest = new SendMessageRequest(smsMessage.senderNumber(), smsMessage.recipientNumber(), smsMessage.messageBody());
        final SendMessageResponse sendMessageResponse = twillioApiCommunicator.sendMessage(sendMessageRequest);
        LOGGER.debug("Successfully sent sms message, response - {}", sendMessageResponse);
        return SmsMessageSendingResult.of(sendMessageResponse.getSid());
    }
}
