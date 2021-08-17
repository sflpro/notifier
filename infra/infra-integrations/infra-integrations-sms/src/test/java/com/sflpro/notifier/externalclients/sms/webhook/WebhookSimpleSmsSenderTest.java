package com.sflpro.notifier.externalclients.sms.webhook;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/28/21
 * Time: 7:19 PM
 */
public class WebhookSimpleSmsSenderTest extends AbstractSmsUnitTest {

    private WebhookSimpleSmsSender simpleSmsSender;

    @Mock
    private WebhookApiRestClient webhookApiRestClient;

    @Before
    public void prepare() {
        simpleSmsSender = new WebhookSimpleSmsSender(webhookApiRestClient);
    }

    @Test
    public void testSend() {
        final SimpleSmsMessage message = SimpleSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "messageBody_" + uuid());

        final WebhookSmsSendMessagesRequest request = new WebhookSmsSendMessagesRequest(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                message.messageBody()
        );

        doNothing().when(webhookApiRestClient).sendMessage(request);
        assertThat(simpleSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(String.valueOf(message.internalId())));

        verify(webhookApiRestClient, times(1)).sendMessage(request);
    }
}
