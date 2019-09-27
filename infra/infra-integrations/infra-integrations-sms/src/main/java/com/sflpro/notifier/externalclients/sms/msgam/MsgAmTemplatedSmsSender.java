package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;


/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/21/19
 * Time: 12:11 PM
 */
class MsgAmTemplatedSmsSender extends AbstractMsgAmSmsSender<TemplatedSmsMessage> implements TemplatedSmsSender {

    private final SmsTemplateContentResolver smsTemplateContentResolver;

    MsgAmTemplatedSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator,
                            final SmsTemplateContentResolver smsTemplateContentResolver) {
        super(msgAmApiCommunicator);
        this.smsTemplateContentResolver = smsTemplateContentResolver;
    }

    @Override
    String bodyFor(final TemplatedSmsMessage message) {
        return message.resolveBodyWith(smsTemplateContentResolver);
    }
}
