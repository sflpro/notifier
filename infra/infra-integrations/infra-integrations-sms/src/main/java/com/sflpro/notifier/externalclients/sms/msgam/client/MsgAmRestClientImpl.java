package com.sflpro.notifier.externalclients.sms.msgam.client;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.msgam.exception.MsgAmClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.msgam.model.request.ClientSmsSendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

/**
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:02 PM
 */
public class MsgAmRestClientImpl implements MsgAmRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgAmRestClientImpl.class);

    /* Properties */
    private final String apiUrl;
    private final RestClient restClient;

    public MsgAmRestClientImpl(final String apiUrl, final RestClient restClient) {
        this.restClient = restClient;
        this.apiUrl = apiUrl;
    }

    @Override
    public SendMessagesResponse sendMessage(final ClientSmsSendMessagesRequest request) {
        LOGGER.debug("MsgAm resource Client Performing send sms message to Msg.AM request with parameters - {}", request);
        final ResponseEntity<SendMessagesResponse> responseEntity = execute(request);
        if (responseEntity.getStatusCode().isError()) {
            LOGGER.error(" Failed to send sms - {}", responseEntity);
            throw new MsgAmClientRuntimeException(request.getMessage().getSourceNumber(), request.getMessage().getPhoneNumber());
        }
        final SendMessagesResponse response = responseEntity.getBody();
        LOGGER.debug("MsgAm resource Client Successfully sent text message with request - {}, response message - {}", request, response);
        return response;
    }

    private ResponseEntity<SendMessagesResponse> execute(final ClientSmsSendMessagesRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        headers.add("Content-Charset", "UTF-8");
        try {
            return restClient.exchange(apiUrl, HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    SendMessagesResponse.class);
        } catch (final Exception ex) {
            throw new MsgAmClientRuntimeException(request.getMessage().getSourceNumber(), request.getMessage().getPhoneNumber(), ex);
        }
    }
}
