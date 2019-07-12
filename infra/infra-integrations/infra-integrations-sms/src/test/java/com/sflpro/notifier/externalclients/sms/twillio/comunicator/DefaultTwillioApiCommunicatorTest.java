package com.sflpro.notifier.externalclients.sms.twillio.comunicator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.DefaultTwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/25/19
 * Time: 11:32 AM
 */
public class DefaultTwillioApiCommunicatorTest extends AbstractSmsUnitTest {

    private TwillioApiCommunicator communicator;

    @Mock
    private TwilioRestClient twillioRestClient;

    @Before
    public void prepare() {
        communicator = new DefaultTwillioApiCommunicator(
                twillioRestClient
        );
    }

    @Test
    public void testSendMessageStatusOk() throws Exception {
        final SendMessageRequest request = new SendMessageRequest(
                uuid(), uuid(), uuid()
        );
        final SendMessageResponse response = new SendMessageResponse(
                uuid(), request.getRecipientNumber(), request.getMessageBody()
        );
        final JSONObject jsonObject = new JSONObject().put("sid", response.getSid());
        jsonObject.put("to", request.getRecipientNumber());
        jsonObject.put("body", request.getMessageBody());
        when(twillioRestClient.request(isA(Request.class))).thenReturn(new Response(jsonObject.toString(), 200));
        when(twillioRestClient.getObjectMapper()).thenReturn(new ObjectMapper());
        assertThat(communicator.sendMessage(request)).isEqualTo(response);
        verify(twillioRestClient).request(isA(Request.class));
        verify(twillioRestClient).getObjectMapper();
    }

    @Test
    public void testSendMessageStatusFailed() throws Exception {
        final SendMessageRequest request = new SendMessageRequest(
                uuid(), uuid(), uuid()
        );
        final SendMessageResponse response = new SendMessageResponse(
                uuid(), request.getRecipientNumber(), request.getMessageBody()
        );
        when(twillioRestClient.request(isA(Request.class))).thenReturn(new Response("", 503));
        when(twillioRestClient.getObjectMapper()).thenReturn(new ObjectMapper());
        assertThatThrownBy(() -> communicator.sendMessage(request))
                .isInstanceOf(TwillioClientRuntimeException.class)
                .hasFieldOrPropertyWithValue("senderNumber", request.getSenderNumber())
                .hasFieldOrPropertyWithValue("recipientNumber", request.getRecipientNumber());
        verify(twillioRestClient).request(isA(Request.class));
        verify(twillioRestClient).getObjectMapper();
    }
}
