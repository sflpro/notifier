package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 4:37 PM
 */
class TwillioTemplatedSmsSender extends AbstractTwillioSmsSender<TemplatedSmsMessage> implements TemplatedSmsSender {

    private final SmsTemplateContentResolver smsTemplateContentResolver;

    TwillioTemplatedSmsSender(final TwillioApiCommunicator twillioApiCommunicator, final SmsTemplateContentResolver smsTemplateContentResolver) {
        super(twillioApiCommunicator);
        this.smsTemplateContentResolver = smsTemplateContentResolver;
    }

    @Override
    String bodyFor(final TemplatedSmsMessage message) {
        return smsTemplateContentResolver.resolve(message.templateId(), message.variables());
    }
}
