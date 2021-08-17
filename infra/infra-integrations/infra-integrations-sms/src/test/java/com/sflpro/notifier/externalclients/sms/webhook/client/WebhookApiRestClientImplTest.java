package com.sflpro.notifier.externalclients.sms.webhook.client;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import com.sflpro.notifier.externalclients.sms.webhook.exception.WebhookSenderClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/28/21
 * Time: 7:55 PM
 */
public class WebhookApiRestClientImplTest extends AbstractSmsUnitTest {

    private WebhookApiRestClientImpl apiRestClient;

    private final String apiUrl = "https://localhost/send";

    @Mock
    private RestClient restClient;

    @Before
    public void prepare() {
        apiRestClient = new WebhookApiRestClientImpl(apiUrl, restClient);
    }

    @Test
    public void sendMessageStatusOk() {
        final WebhookSmsSendMessagesRequest request = new WebhookSmsSendMessagesRequest(1L, uuid(), uuid(), uuid());
        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request), Void.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        apiRestClient.sendMessage(request);
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request), Void.class);
    }

    @Test
    public void sendMessageStatusFailed() {
        final WebhookSmsSendMessagesRequest request = new WebhookSmsSendMessagesRequest(1L, uuid(), uuid(), uuid());

        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request), Void.class)).thenReturn(new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE));
        assertThatThrownBy(() -> apiRestClient.sendMessage(request))
                .isInstanceOf(WebhookSenderClientRuntimeException.class)
                .hasFieldOrPropertyWithValue("senderNumber", request.getSenderNumber())
                .hasFieldOrPropertyWithValue("recipientNumber", request.getRecipientNumber());
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request), Void.class);
    }
}
