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

import java.time.LocalDateTime;
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

    private String operatorId = "operatorId_" + uuid();
    private String operatorName = "operator_name" + uuid();

    @Before
    public void prepare() {
        nikitamobileTemplatedSmsSender = new NikitamobileTemplatedSmsSender(
                nikitamobileApiCommunicator, smsTemplateContentResolver, operatorId, operatorName
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
        requestMessage.setOperatorId(operatorId);
        requestMessage.setOperator(operatorName);
        requestMessage.setSubmitDate(LocalDateTime.now());
        final SendMessageRequest request = new SendMessageRequest(
                requestMessage
        );
        final SendMessageResponse response = new SendMessageResponse(
                new com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.Message(request.getMessage().getId())
        );
        when(smsTemplateContentResolver.resolve(message.templateId(), message.variables())).then(invocation -> messageBody);
        when(nikitamobileApiCommunicator.sendMessage(isA(SendMessageRequest.class))).then(invocation -> {
            final SendMessageRequest requestArgument = invocation.getArgument(0);
            assertThat(requestArgument.getMessage())
                    .hasFieldOrPropertyWithValue("id", requestMessage.getId())
                    .hasFieldOrPropertyWithValue("recipientNumber", requestMessage.getRecipientNumber())
                    .hasFieldOrPropertyWithValue("senderNumber", requestMessage.getSenderNumber())
                    .hasFieldOrPropertyWithValue("messageCount", requestMessage.getMessageCount())
                    .hasFieldOrPropertyWithValue("content", requestMessage.getContent())
                    .hasFieldOrPropertyWithValue("operatorId", requestMessage.getOperatorId())
                    .hasFieldOrPropertyWithValue("operator", requestMessage.getOperator())
                    .hasFieldOrPropertyWithValue("priority", requestMessage.getPriority());
            return response;
        });
        assertThat(nikitamobileTemplatedSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getMessage().getId().toString()));
        verify(smsTemplateContentResolver).resolve(message.templateId(), message.variables());
        verify(nikitamobileApiCommunicator).sendMessage(any(SendMessageRequest.class));
    }
}
