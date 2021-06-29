package com.sflpro.notifier.externalclients.sms.webhook;

import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:46 AM
 */
class WebhookSimpleSmsSender extends AbstractWebhookSmsSender<SimpleSmsMessage> implements SimpleSmsSender {

    WebhookSimpleSmsSender(final WebhookApiRestClient apiRestClient) {
        super(apiRestClient);
    }

    @Override
    String buildMessageBody(final SimpleSmsMessage message) {
        return message.messageBody();
    }
}
