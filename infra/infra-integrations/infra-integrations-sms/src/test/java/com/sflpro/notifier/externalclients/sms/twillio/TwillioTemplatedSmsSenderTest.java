package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 5:15 PM
 */
public class TwillioTemplatedSmsSenderTest extends AbstractSmsUnitTest {

    private TemplatedSmsSender twillioTemplatedSmsSender;

    @Mock
    private TwillioApiCommunicator twillioApiCommunicator;

    @Mock
    private SmsTemplateContentResolver smsTemplateContentResolver;

    @Before
    public void prepare() {
        twillioTemplatedSmsSender = new TwillioTemplatedSmsSender(
                twillioApiCommunicator,
                smsTemplateContentResolver
        );
    }

    @Test
    public void testSend() {
        final TemplatedSmsMessage message = TemplatedSmsMessage.of(1L,
                "sender_" + uuid(),
                "recipientNumber_" + uuid(),
                "templateId" + uuid(), Collections.singletonMap("name", uuid()));
        final String msgBody = "Hello {name}".replace("{name}", (String) message.variables().get("name"));
        final SendMessageRequest request = new SendMessageRequest(
                message.sender(), message.recipientNumber(), msgBody
        );
        final SendMessageResponse response = new SendMessageResponse(
                uuid(), message.recipientNumber(), msgBody
        );
        when(smsTemplateContentResolver.resolve(message.templateId(), message.variables())).then(invocation -> msgBody);
        when(twillioApiCommunicator.sendMessage(request)).then(invocation -> response);
        assertThat(twillioTemplatedSmsSender.send(message)).isEqualTo(SmsMessageSendingResult.of(response.getSid()));
        verify(twillioApiCommunicator).sendMessage(request);
        verify(smsTemplateContentResolver).resolve(message.templateId(), message.variables());
    }
}
