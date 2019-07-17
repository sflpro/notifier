package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:46 AM
 */
class NikitamobileSimpleSmsSender extends AbstractNikitamobileSmsSender<SimpleSmsMessage> implements SimpleSmsSender {

    NikitamobileSimpleSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator, final String login, final String password, final String version) {
        super(nikitamobileApiCommunicator, login, password, version);
    }

    @Override
    String bodyFor(final SimpleSmsMessage message) {
        return message.messageBody();
    }
}
