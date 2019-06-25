package com.sflpro.notifier.externalclients.sms.msgam.client;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.msgam.exception.MsgAmClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.ClientSmsSendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.Message;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/25/19
 * Time: 11:19 AM
 */
public class MsgAmRestClientImplTest extends AbstractSmsUnitTest {

    private MsgAmRestClient client;

    private String apiUrl = "https://msg.am/api";

    @Mock
    private RestClient restClient;

    @Before
    public void prepare() {
        client = new MsgAmRestClientImpl(
                apiUrl, restClient
        );
    }

    @Test
    public void sendMessageStatusOk() {
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                1L,
                uuid(),
                uuid(),
                uuid(),
                uuid(),
                uuid());
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(), uuid(), uuid(), new Message(
                clientSmsSendMessagesRequest.getMessage().getId(), clientSmsSendMessagesRequest.getMessage().getPhoneNumber(), 1L, 1
        )
        );
        response.setResult("0");
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        headers.add("Content-Charset", "UTF-8");
        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(clientSmsSendMessagesRequest, headers), SendMessagesResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        assertThat(client.sendMessage(clientSmsSendMessagesRequest)).isEqualTo(response);
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(clientSmsSendMessagesRequest, headers), SendMessagesResponse.class);
    }

    @Test
    public void sendMessageStatusFailed() {
        final ClientSmsSendMessagesRequest clientSmsSendMessagesRequest = new ClientSmsSendMessagesRequest(
                "sms_send",
                1L,
                uuid(),
                uuid(),
                uuid(),
                uuid(),
                uuid());
        final SendMessagesResponse response = new SendMessagesResponse(
                uuid(), uuid(), uuid(), new Message(
                clientSmsSendMessagesRequest.getMessage().getId(), clientSmsSendMessagesRequest.getMessage().getPhoneNumber(), 1L, 1
        )
        );
        response.setResult("0");
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        headers.add("Content-Charset", "UTF-8");
        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(clientSmsSendMessagesRequest, headers), SendMessagesResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE));
        assertThatThrownBy(() -> client.sendMessage(clientSmsSendMessagesRequest))
                .isInstanceOf(MsgAmClientRuntimeException.class)
                .hasFieldOrPropertyWithValue("senderNumber", clientSmsSendMessagesRequest.getMessage().getSourceNumber())
                .hasFieldOrPropertyWithValue("recipientNumber", clientSmsSendMessagesRequest.getMessage().getPhoneNumber());
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(clientSmsSendMessagesRequest, headers), SendMessagesResponse.class);
    }
}
