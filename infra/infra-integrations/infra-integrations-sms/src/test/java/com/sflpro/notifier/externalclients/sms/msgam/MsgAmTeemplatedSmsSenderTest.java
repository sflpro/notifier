package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.Message;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 2:12 PM
 */
public class MsgAmTeemplatedSmsSenderTest extends AbstractSmsUnitTest {

    private TemplatedSmsSender msgAmTemplatedSmsSender;

    @Mock
    private MsgAmApiCommunicator msgAmApiCommunicator;

    @Mock
    private SmsTemplateContentResolver smsTemplateContentResolver;

    @Before
    public void prepare() {
        msgAmTemplatedSmsSender = new MsgAmTemplatedSmsSender(
                msgAmApiCommunicator,
                smsTemplateContentResolver
        );
    }

    @Test
    public void testSend() {
        final Map<String, String> variables = Collections.singletonMap("name", uuid());
        final TemplatedSmsMessage message = TemplatedSmsMessage.of(1L, "sender_" + uuid(),
                "recipientNumber_" + uuid(), "templateId_" + uuid(), variables);
        final String msgBody = "Hello {name}".replace("{name}", (String) message.variables().get("name"));
        final SendMessagesRequest request = new SendMessagesRequest(
                message.internalId(), message.sender(), message.recipientNumber(), msgBody
        );
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(),
                uuid(),
                uuid(),
                new Message(
                        request.getMessageId(),
                        uuid(),
                        BigInteger.ONE,
                        1
                )
        );
        when(smsTemplateContentResolver.resolve(message.templateId(), message.variables())).then(invocation -> msgBody);
        when(msgAmApiCommunicator.sendMessage(request)).thenAnswer(invocation -> response);
        assertThat(msgAmTemplatedSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getSid()));
        verify(smsTemplateContentResolver).resolve(message.templateId(), message.variables());
        verify(msgAmApiCommunicator).sendMessage(request);
    }
}
