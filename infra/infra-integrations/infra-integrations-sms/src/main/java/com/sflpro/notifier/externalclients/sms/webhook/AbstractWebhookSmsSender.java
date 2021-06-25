package com.sflpro.notifier.externalclients.sms.webhook;

import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;
import com.sflpro.notifier.spi.sms.SmsMessage;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 4:05 PM
 */
public abstract class AbstractWebhookSmsSender<M extends SmsMessage> implements SmsSender<M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWebhookSmsSender.class);

    private final WebhookApiRestClient apiRestClient;

    public AbstractWebhookSmsSender(final WebhookApiRestClient apiRestClient) {
        this.apiRestClient = apiRestClient;
    }

    @Override
    public SmsMessageSendingResult send(final M message) {
        logger.trace("Sending sms message with request model - {}", message);

        final WebhookSmsSendMessagesRequest request = new WebhookSmsSendMessagesRequest(
                message.internalId(),
                message.sender(),
                message.recipientNumber(),
                buildMessageBody(message)
        );
        apiRestClient.sendMessage(request);

        logger.debug("Successfully sent sms message with request - {}", message);
        return SmsMessageSendingResult.of(String.valueOf(message.internalId()));
    }

    abstract String buildMessageBody(final M message);
}
