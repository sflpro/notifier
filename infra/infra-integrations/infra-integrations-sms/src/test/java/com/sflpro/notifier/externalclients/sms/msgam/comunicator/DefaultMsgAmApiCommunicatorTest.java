package com.sflpro.notifier.externalclients.sms.msgam.comunicator;

import com.sflpro.notifier.externalclients.sms.msgam.client.MsgAmRestClient;
import com.sflpro.notifier.externalclients.sms.msgam.communicator.DefaultMsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.exception.MsgAmClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.ClientSmsSendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.Message;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/25/19
 * Time: 11:01 AM
 */
public class DefaultMsgAmApiCommunicatorTest extends AbstractSmsUnitTest {

    private MsgAmApiCommunicator communicator;

    private String login = "user";

    private String pass = "secret";

    @Mock
    private MsgAmRestClient msgAmRestClient;

    @Before
    public void prepare() {
        communicator = new DefaultMsgAmApiCommunicator(login, pass, msgAmRestClient);
    }

    @Test
    public void testSendMessageStatusOk() {
        final SendMessagesRequest sendMessagesRequest = new SendMessagesRequest(
                1L, uuid(), uuid(), uuid()
        );
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(), uuid(), uuid(), new Message(
                sendMessagesRequest.getMessageId(), sendMessagesRequest.getRecipientNumber(), 1L, 1
            )
        );
        response.setResult("0");
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                sendMessagesRequest.getMessageId(),
                sendMessagesRequest.getSenderNumber(),
                login,
                pass,
                sendMessagesRequest.getRecipientNumber(),
                sendMessagesRequest.getMessageBody());
        when(msgAmRestClient.sendMessage(clientSmsSendMessagesRequest)).thenReturn(response);
        assertThat(communicator.sendMessage(sendMessagesRequest)).isEqualTo(response);
        verify(msgAmRestClient).sendMessage(clientSmsSendMessagesRequest);
    }

    @Test
    public void testSendMessageStatusFailed() {
        final SendMessagesRequest sendMessagesRequest = new SendMessagesRequest(
                1L, uuid(), uuid(), uuid()
        );
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(), uuid(), uuid(), new Message(
                sendMessagesRequest.getMessageId(), sendMessagesRequest.getRecipientNumber(), 1L, 1
        )
        );
        response.setResult("0");
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                sendMessagesRequest.getMessageId(),
                sendMessagesRequest.getSenderNumber(),
                login,
                pass,
                sendMessagesRequest.getRecipientNumber(),
                sendMessagesRequest.getMessageBody());
        final MsgAmClientRuntimeException exception = new MsgAmClientRuntimeException(sendMessagesRequest.getSenderNumber(), sendMessagesRequest.getRecipientNumber());
        when(msgAmRestClient.sendMessage(clientSmsSendMessagesRequest))
                .thenThrow(exception);
        assertThatThrownBy(() -> communicator.sendMessage(sendMessagesRequest)).isEqualTo(exception);
        verify(msgAmRestClient).sendMessage(clientSmsSendMessagesRequest);
    }

    @Test
    public void testSendMessageResponseStatusFailed() {
        final SendMessagesRequest sendMessagesRequest = new SendMessagesRequest(
                1L, uuid(), uuid(), uuid()
        );
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(), uuid(), uuid(), new Message(
                sendMessagesRequest.getMessageId(), sendMessagesRequest.getRecipientNumber(), 1L, 1
        )
        );
        response.setResult("1");
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                sendMessagesRequest.getMessageId(),
                sendMessagesRequest.getSenderNumber(),
                login,
                pass,
                sendMessagesRequest.getRecipientNumber(),
                sendMessagesRequest.getMessageBody());
        final MsgAmClientRuntimeException exception = new MsgAmClientRuntimeException(sendMessagesRequest.getSenderNumber(), sendMessagesRequest.getRecipientNumber());
        when(msgAmRestClient.sendMessage(clientSmsSendMessagesRequest)).thenReturn(response);
        assertThatThrownBy(() -> communicator.sendMessage(sendMessagesRequest))
                .hasFieldOrPropertyWithValue("senderNumber", exception.getSenderNumber())
                .hasFieldOrPropertyWithValue("recipientNumber", exception.getRecipientNumber());
        verify(msgAmRestClient).sendMessage(clientSmsSendMessagesRequest);
    }
}
