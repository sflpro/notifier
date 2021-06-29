package com.sflpro.notifier.externalclients.sms.webhook;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/28/21
 * Time: 7:19 PM
 */
public class WebhookTemplatedSmsSenderTest extends AbstractSmsUnitTest {

    private WebhookTemplatedSmsSender templatedSmsSender;

    @Mock
    private WebhookApiRestClient webhookApiRestClient;

    @Mock
    private SmsTemplateContentResolver smsTemplateContentResolver;

    @Before
    public void prepare() {
        templatedSmsSender = new WebhookTemplatedSmsSender(webhookApiRestClient, smsTemplateContentResolver);
    }

    @Test
    public void testSend() {
        final Map<String, String> variables = Collections.singletonMap("name", uuid());
        final TemplatedSmsMessage message = TemplatedSmsMessage.of(1L, "sender_" + uuid(),
                "recipientNumber_" + uuid(), "templateId_" + uuid(), variables);
        final String msgBody = "Hello {name}".replace("{name}", (String) message.variables().get("name"));

        final WebhookSmsSendMessagesRequest request = new WebhookSmsSendMessagesRequest(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                msgBody
        );

        when(smsTemplateContentResolver.resolve(message.templateId(), message.variables())).thenReturn(msgBody);
        doNothing().when(webhookApiRestClient).sendMessage(request);
        assertThat(templatedSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(String.valueOf(message.internalId())));

        verify(smsTemplateContentResolver, times(1)).resolve(message.templateId(), message.variables());
        verify(webhookApiRestClient, times(1)).sendMessage(request);
    }
}
