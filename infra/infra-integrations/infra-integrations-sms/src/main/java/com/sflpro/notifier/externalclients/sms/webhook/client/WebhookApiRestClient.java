package com.sflpro.notifier.externalclients.sms.webhook.client;

import com.sflpro.notifier.externalclients.sms.webhook.request.WebhookSmsSendMessagesRequest;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 5:27 PM
 */
public interface WebhookApiRestClient {

    void sendMessage(final WebhookSmsSendMessagesRequest messagesRequest);
}
