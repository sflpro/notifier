package com.sflpro.notifier.externalclients.sms.msgam.communicator;

import com.sflpro.notifier.externalclients.sms.msgam.client.MsgAmRestClient;
import com.sflpro.notifier.externalclients.sms.msgam.exception.MsgAmClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.ClientSmsSendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 4:52 PM
 */
public class DefaultMsgAmApiCommunicator implements MsgAmApiCommunicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMsgAmApiCommunicator.class);

    /* Properties */
    @Value("${msgam.account.user}")
    private final String login;

    @Value("${msgam.account.pass}")
    private final String pass;

    private final MsgAmRestClient msgAmRestClient;

    /* Constructors */
    public DefaultMsgAmApiCommunicator(final String login,
                                       final String pass,
                                       final MsgAmRestClient msgAmRestClient) {
        this.login = login;
        this.pass = pass;
        this.msgAmRestClient = msgAmRestClient;
    }

    @Override
    public SendMessagesResponse sendMessage(final SendMessagesRequest sendMessagesRequest) {
        Assert.notNull(sendMessagesRequest, "Null was passed as ana rgument for parameter 'sendMessagesRequest'.");
        LOGGER.debug("Communicator Performing send sms message to Msg.AM request with parameters - {}", sendMessagesRequest);
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                sendMessagesRequest.getMessageId(),
                sendMessagesRequest.getSenderNumber(),
                login,
                pass,
                sendMessagesRequest.getRecipientNumber().replace("+", ""),
                sendMessagesRequest.getMessageBody());
        final SendMessagesResponse message = msgAmRestClient.sendMessage(clientSmsSendMessagesRequest);
        if (!message.isSuccess()) {
            LOGGER.error("Error occurred while sending sms message by Msg.am e - {}", message.getDescription());
            throw new MsgAmClientRuntimeException(
                    sendMessagesRequest.getSenderNumber(),
                    sendMessagesRequest.getRecipientNumber()
            );
        }
        LOGGER.debug("Msg Am Communicator Successfully sent text message with request - {}, response message - {}", sendMessagesRequest, message);
        return message;
    }
}
