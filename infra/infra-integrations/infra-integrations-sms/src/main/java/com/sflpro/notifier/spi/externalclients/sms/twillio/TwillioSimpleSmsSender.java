package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.sms.SimpleSmsMessage;
import com.sflpro.notifier.sms.SimpleSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 4:37 PM
 */
class TwillioSimpleSmsSender extends AbstractTwillioSmsSender<SimpleSmsMessage> implements SimpleSmsSender {

    TwillioSimpleSmsSender(final TwillioApiCommunicator twillioApiCommunicator) {
       super(twillioApiCommunicator);
    }

    @Override
    String bodyFor(final SimpleSmsMessage message) {
        return message.messageBody();
    }
}
