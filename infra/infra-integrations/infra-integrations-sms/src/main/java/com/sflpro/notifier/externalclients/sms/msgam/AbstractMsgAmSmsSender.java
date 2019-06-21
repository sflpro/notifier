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

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMsgAmSmsSender.class);

    private final MsgAmApiCommunicator msgAmApiCommunicator;
    private final MsgAmMessageIdProvider msgAmMessageIdProvider;

    AbstractMsgAmSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator,
                           final MsgAmMessageIdProvider msgAmMessageIdProvider) {
        this.msgAmApiCommunicator = msgAmApiCommunicator;
        this.msgAmMessageIdProvider = msgAmMessageIdProvider;
    }

    @Override
    public SmsMessageSendingResult send(final M message) {
        LOGGER.debug("Sending sms message with request model - {}", message);
        final long messageId = msgAmMessageIdProvider.nextId();
        final SendMessagesResponse sendMessagesResponse = msgAmApiCommunicator.sendMessage(new SendMessagesRequest(
                messageId,
                message.sender(),
                message.recipientNumber(),
                bodyFor(message))
        );
        LOGGER.debug("Successfully sent sms message, response - {}", sendMessagesResponse);
        return SmsMessageSendingResult.of(sendMessagesResponse.getSid());
    }

    abstract String bodyFor(final M message);
}
