package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import com.sflpro.notifier.spi.sms.SmsMessage;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/21/19
 * Time: 12:12 PM
 */
abstract class AbstractMsgAmSmsSender<M extends SmsMessage> implements SmsSender<M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMsgAmSmsSender.class);

    private final MsgAmApiCommunicator msgAmApiCommunicator;

    AbstractMsgAmSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator) {
        this.msgAmApiCommunicator = msgAmApiCommunicator;
    }

    @Override
    public SmsMessageSendingResult send(final M message) {
        logger.debug("Sending sms message with request model - {}", message);
        final SendMessagesResponse sendMessagesResponse = msgAmApiCommunicator.sendMessage(new SendMessagesRequest(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                bodyFor(message))
        );
        logger.debug("Successfully sent sms message, response - {}", sendMessagesResponse);
        return SmsMessageSendingResult.of(sendMessagesResponse.getSid());
    }

    abstract String bodyFor(final M message);
}
