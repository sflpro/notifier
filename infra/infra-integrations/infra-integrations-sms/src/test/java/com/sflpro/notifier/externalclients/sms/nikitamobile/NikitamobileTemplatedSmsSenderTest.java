package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.Content;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.Message;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 5:23 PM
 */
public class NikitamobileTemplatedSmsSenderTest extends AbstractSmsUnitTest {

    private TemplatedSmsSender nikitamobileTemplatedSmsSender;

    @Mock
    private NikitamobileApiCommunicator nikitamobileApiCommunicator;

    @Mock
    private SmsTemplateContentResolver smsTemplateContentResolver;

    private final String login = "login" + uuid();
    private final String password = "password" + uuid();
    private final String version = "1.0";

    @Before
    public void prepare() {
        nikitamobileTemplatedSmsSender = new NikitamobileTemplatedSmsSender(
                nikitamobileApiCommunicator, smsTemplateContentResolver, login, password, version
        );
    }

    @Test
    public void testSend() {
        final TemplatedSmsMessage message = TemplatedSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "templateId" + uuid(),
                Collections.singletonMap("name", uuid())
        );
        final String messageBody = "Hello {name}".replace("{name}", (String) message.variables().get("name"));
        final Message requestMessage = new Message(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                new Content(
                        message.contentType(),
                        messageBody
                )
        );
        final SendMessageRequest request = new SendMessageRequest(
                requestMessage
        );
        request.setLogin(login);
        request.setPassword(password);
        final SendMessageResponse response = new SendMessageResponse();
        when(smsTemplateContentResolver.resolve(message.templateId(), message.variables())).thenReturn(messageBody);
        when(nikitamobileApiCommunicator.sendMessage(isA(SendMessageRequest.class))).then(invocation -> {
            final SendMessageRequest requestArgument = invocation.getArgument(0);
            assertThat(requestArgument)
                    .hasFieldOrPropertyWithValue("login", login)
                    .hasFieldOrPropertyWithValue("password", password)
                    .hasFieldOrPropertyWithValue("version", version);
            assertThat(requestArgument.getMessage())
                    .hasFieldOrPropertyWithValue("id", requestMessage.getId())
                    .hasFieldOrPropertyWithValue("recipientNumber", requestMessage.getRecipientNumber())
                    .hasFieldOrPropertyWithValue("senderNumber", requestMessage.getSenderNumber())
                    .hasFieldOrPropertyWithValue("content", requestMessage.getContent())
                    .hasFieldOrPropertyWithValue("priority", requestMessage.getPriority());
            return response;
        });
        assertThat(nikitamobileTemplatedSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(String.valueOf(message.internalId())));
        verify(smsTemplateContentResolver).resolve(message.templateId(), message.variables());
        verify(nikitamobileApiCommunicator).sendMessage(any(SendMessageRequest.class));
    }
}
