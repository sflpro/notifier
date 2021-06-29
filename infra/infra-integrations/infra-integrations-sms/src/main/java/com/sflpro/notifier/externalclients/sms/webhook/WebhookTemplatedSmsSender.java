package com.sflpro.notifier.externalclients.sms.webhook;

import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 4:06 PM
 */
class WebhookTemplatedSmsSender extends AbstractWebhookSmsSender<TemplatedSmsMessage> implements TemplatedSmsSender {

    private final SmsTemplateContentResolver smsTemplateContentResolver;

    WebhookTemplatedSmsSender(final WebhookApiRestClient apiRestClient,
                                     final SmsTemplateContentResolver smsTemplateContentResolver) {
        super(apiRestClient);
        this.smsTemplateContentResolver = smsTemplateContentResolver;
    }

    @Override
    String buildMessageBody(final TemplatedSmsMessage message) {
        return message.resolveBodyWith(smsTemplateContentResolver);
    }
}
