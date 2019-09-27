package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:46 AM
 */
class NikitamobileTemplatedSmsSender extends AbstractNikitamobileSmsSender<TemplatedSmsMessage> implements TemplatedSmsSender {

    private final SmsTemplateContentResolver smsTemplateContentResolver;

    NikitamobileTemplatedSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator,
                                   final SmsTemplateContentResolver smsTemplateContentResolver,
                                   final String operatorId,
                                   final String operatorName,
                                   final String version) {
        super(nikitamobileApiCommunicator, operatorId, operatorName, version);
        this.smsTemplateContentResolver = smsTemplateContentResolver;
    }

    @Override
    String bodyFor(final TemplatedSmsMessage message) {
        return message.resolveBodyWith(smsTemplateContentResolver);
    }
}
