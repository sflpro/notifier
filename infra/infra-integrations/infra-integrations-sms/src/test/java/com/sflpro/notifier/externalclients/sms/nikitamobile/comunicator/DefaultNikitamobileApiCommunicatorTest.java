package com.sflpro.notifier.externalclients.sms.nikitamobile.comunicator;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.DefaultNikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.exception.NikitamobileClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.Content;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.Message;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;
import com.sflpro.notifier.externalclients.sms.test.AbstractSmsUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/25/19
 * Time: 10:32 AM
 */
public class DefaultNikitamobileApiCommunicatorTest extends AbstractSmsUnitTest {


    private NikitamobileApiCommunicator communicator;

    @Mock
    private RestClient restClient;
    private final String apiUrl = "https://test.com";


    @Before
    public void prepare() {
        communicator = new DefaultNikitamobileApiCommunicator(restClient, apiUrl);
    }

    @Test
    public void testSendMessageStatusOk() {
        final Message message = new Message(
                1L, uuid(), uuid(), new Content(uuid(), uuid())
        );
        final SendMessageRequest request = new SendMessageRequest(message);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, new MediaType(MediaType.APPLICATION_XML, StandardCharsets.US_ASCII).toString());
        final SendMessageResponse response = new SendMessageResponse();
        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request, headers), SendMessageResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        assertThat(communicator.sendMessage(request)).isEqualTo(response);
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request, headers), SendMessageResponse.class);
    }

    @Test
    public void testSendMessageStatusFailed() {
        final Message message = new Message(
                1L, uuid(), uuid(), new Content(uuid(), uuid())
        );
        final SendMessageRequest request = new SendMessageRequest(message);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, new MediaType(MediaType.APPLICATION_XML, StandardCharsets.US_ASCII).toString());
        final SendMessageResponse response = new SendMessageResponse();
        when(restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request, headers), SendMessageResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
        assertThatThrownBy(() -> communicator.sendMessage(request)).isInstanceOf(NikitamobileClientRuntimeException.class);
        verify(restClient).exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(request, headers), SendMessageResponse.class);
    }


}
