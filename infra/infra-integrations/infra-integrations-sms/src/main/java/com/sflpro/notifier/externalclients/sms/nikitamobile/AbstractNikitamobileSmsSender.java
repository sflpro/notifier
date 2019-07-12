package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.Content;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.Message;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;
import com.sflpro.notifier.spi.sms.SmsMessage;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsSender;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:28 AM
 */
abstract class AbstractNikitamobileSmsSender<M extends SmsMessage> implements SmsSender<M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNikitamobileSmsSender.class);

    private final NikitamobileApiCommunicator nikitamobileApiCommunicator;

    private final String operatorId;
    private final String operatorName;


    AbstractNikitamobileSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator, final String operatorId, final String operatorName) {
        this.nikitamobileApiCommunicator = nikitamobileApiCommunicator;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }

    @Override
    public SmsMessageSendingResult send(final M message) {
        Assert.notNull(message, "Null was passed as an argument for parameter 'message'.");
        logger.debug("Sending sms via ");
        final Message requestMessage = new Message(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                new Content(
                        message.contentType(),
                        bodyFor(message)
                )
        );
        requestMessage.setOperatorId(operatorId);
        requestMessage.setOperator(operatorName);
        final SendMessageResponse sendMessageResponse = nikitamobileApiCommunicator.sendMessage(new SendMessageRequest(requestMessage));
        return SmsMessageSendingResult.of(String.valueOf(sendMessageResponse.getMessage().getId()));
    }

    abstract String bodyFor(final M message);
}
