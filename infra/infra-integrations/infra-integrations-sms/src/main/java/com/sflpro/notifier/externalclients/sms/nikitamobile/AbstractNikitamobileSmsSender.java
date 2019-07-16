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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:28 AM
 */
abstract class AbstractNikitamobileSmsSender<M extends SmsMessage> implements SmsSender<M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNikitamobileSmsSender.class);

    private final NikitamobileApiCommunicator nikitamobileApiCommunicator;

    private final String login;
    private final String password;
    private final String version;


    AbstractNikitamobileSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator,
                                  final String login,
                                  final String password,
                                  final String version) {
        this.nikitamobileApiCommunicator = nikitamobileApiCommunicator;
        this.login = login;
        this.password = password;
        this.version = version;
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
        requestMessage.setValidityPeriod(1);
        final SendMessageRequest request = new SendMessageRequest(requestMessage);
        request.setLogin(login);
        request.setPassword(password);
        request.setRefId(NikitamobileDateTimeUtil.format(LocalDateTime.now()));
        request.setVersion(version);
        final SendMessageResponse sendMessageResponse = nikitamobileApiCommunicator.sendMessage(request);
        return SmsMessageSendingResult.of(String.valueOf(message.internalId()));
    }

    abstract String bodyFor(final M message);
}
