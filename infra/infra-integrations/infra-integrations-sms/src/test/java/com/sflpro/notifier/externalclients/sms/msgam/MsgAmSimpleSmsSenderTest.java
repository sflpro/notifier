package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.Message;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 2:12 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class MsgAmSimpleSmsSenderTest extends AbstractSmsUnitTest {

    private SimpleSmsSender msgAmSimpleSmsSender;

    @Mock
    private MsgAmApiCommunicator msgAmApiCommunicator;

    @Before
    public void prepare() {
        msgAmSimpleSmsSender = new MsgAmSimpleSmsSender(
                msgAmApiCommunicator
        );
    }

    @Test
    public void testSend() {
        final SimpleSmsMessage message = SimpleSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "messageBody_" + uuid());
        final SendMessagesRequest request = new SendMessagesRequest(
                message.internalId(), message.sender(), message.recipientNumber(), message.messageBody()
        );
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(),
                uuid(),
                uuid(),
                new Message(
                        request.getMessageId(),
                        uuid(),
                        1L,
                        1
                )
        );
        when(msgAmApiCommunicator.sendMessage(request)).thenReturn(response);
        assertThat(msgAmSimpleSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getSid()));
        verify(msgAmApiCommunicator).sendMessage(request);
    }
}
