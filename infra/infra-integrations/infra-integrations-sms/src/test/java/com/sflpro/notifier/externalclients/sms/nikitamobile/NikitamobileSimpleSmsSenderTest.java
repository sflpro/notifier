package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.Content;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.Message;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

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
public class NikitamobileSimpleSmsSenderTest extends AbstractSmsUnitTest {

    private SimpleSmsSender nikitamobileSimpleSmsSender;

    @Mock
    private NikitamobileApiCommunicator nikitamobileApiCommunicator;

    private String operatorId = "operatorId_" + uuid();
    private String operatorName = "operator_name" + uuid();

    @Before
    public void prepare() {
        nikitamobileSimpleSmsSender = new NikitamobileSimpleSmsSender(
                nikitamobileApiCommunicator, operatorId, operatorName
        );
    }

    @Test
    public void testSend() {
        final SimpleSmsMessage message = SimpleSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "messageBody_" + uuid());
        final Message requestMessage = new Message(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                new Content(
                        message.contentType(),
                        message.messageBody()
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
        assertThat(nikitamobileSimpleSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getMessage().getId().toString()));
        verify(nikitamobileApiCommunicator).sendMessage(any(SendMessageRequest.class));
    }
}
