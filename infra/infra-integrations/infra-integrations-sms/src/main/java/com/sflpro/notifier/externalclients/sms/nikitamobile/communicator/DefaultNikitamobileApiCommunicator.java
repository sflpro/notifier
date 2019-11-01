package com.sflpro.notifier.externalclients.sms.nikitamobile.communicator;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.sms.nikitamobile.exception.NikitamobileClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:26 PM
 */
public class DefaultNikitamobileApiCommunicator implements NikitamobileApiCommunicator {

    private static final Logger logger = LoggerFactory.getLogger(DefaultNikitamobileApiCommunicator.class);

    /* Properties */
    private final String apiUrl;
    private final RestClient restClient;

    public DefaultNikitamobileApiCommunicator(final RestClient restClient, final String apiUrl) {
        this.restClient = restClient;
        this.apiUrl = apiUrl;
    }

    @Override
    @Nonnull
    public SendMessageResponse sendMessage(@Nonnull final SendMessageRequest request) {
        try {
            logger.debug("Preparing to send text message with request - {}", request);
            final HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
            headers.add(
                    HttpHeaders.CONTENT_TYPE,
                    new MediaType(MediaType.APPLICATION_XML, StandardCharsets.US_ASCII).toString()
            );
            final ResponseEntity<SendMessageResponse> response = restClient.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    SendMessageResponse.class
            );
            if (response.getStatusCode().isError()) {
                logger.error("Failed to send sms - {}", response);
                throw new NikitamobileClientRuntimeException(request.getMessage().getSenderNumber(), request.getMessage().getRecipientNumber());
            }
            final SendMessageResponse sendMessageResponse = response.getBody();
            logger.debug("Created response model for send message request - {}", sendMessageResponse);
            return sendMessageResponse;
        } catch (final Exception ex) {
            logger.error("Error occurred while sending sms message via Nikitamobile API.");
            throw new NikitamobileClientRuntimeException(
                    request.getMessage().getSenderNumber(),
                    request.getMessage().getRecipientNumber(),
                    ex);
        }
    }
}
