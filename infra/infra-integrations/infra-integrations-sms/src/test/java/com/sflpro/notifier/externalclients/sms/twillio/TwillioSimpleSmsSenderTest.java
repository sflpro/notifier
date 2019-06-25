package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 5:08 PM
 */
public class TwillioSimpleSmsSenderTest extends AbstractSmsUnitTest {

    private SimpleSmsSender twillioSimpleSmsSender;

    @Mock
    private TwillioApiCommunicator twillioApiCommunicator;

    @Before
    public void prepare() {
        twillioSimpleSmsSender = new TwillioSimpleSmsSender(
                twillioApiCommunicator
        );
    }

    @Test
    public void testSend() {
        final SimpleSmsMessage message = SimpleSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "messageBody_" + uuid());
        final SendMessageRequest request = new SendMessageRequest(
                message.sender(), message.recipientNumber(), message.messageBody()
        );
        final SendMessageResponse response = new SendMessageResponse(
                uuid(), message.recipientNumber(), message.messageBody()
        );
        when(twillioApiCommunicator.sendMessage(request)).thenReturn(response);
        assertThat(twillioSimpleSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getSid()));
        verify(twillioApiCommunicator).sendMessage(request);
    }

}
