package com.sflpro.notifier.externalclients.sms.webhook.client;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.webhook.exception.WebhookSenderClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 5:43 PM
 */
public class WebhookApiRestClientImpl implements WebhookApiRestClient {

    private static final Logger logger = LoggerFactory.getLogger(WebhookApiRestClientImpl.class);

    private final String apiUrl;
    private final RestClient restClient;

    public WebhookApiRestClientImpl(final String apiUrl, final RestClient restClient) {
        this.apiUrl = apiUrl;
        this.restClient = restClient;
    }

    @Override
    public void sendMessage(final WebhookSmsSendMessagesRequest messagesRequest) {
        logger.trace("Webhook sender resource Client Performing send sms message to {} with parameters - {}", apiUrl, messagesRequest);

        final ResponseEntity<Void> responseEntity = execute(messagesRequest);

        if (responseEntity.getStatusCode().isError()) {
            logger.error("Failed to send sms - {}", responseEntity);
            throw new WebhookSenderClientRuntimeException(messagesRequest.getSenderNumber(), messagesRequest.getRecipientNumber());
        }

        logger.debug("Webhook sender resource Client Successfully sent text message with request - {}", messagesRequest);
    }

    private ResponseEntity<Void> execute(final WebhookSmsSendMessagesRequest messagesRequest) {
        try {
            return restClient.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(messagesRequest), Void.class);
        } catch (final Exception ex) {
            throw new WebhookSenderClientRuntimeException(messagesRequest.getSenderNumber(), messagesRequest.getRecipientNumber(), ex);
        }
    }
}
