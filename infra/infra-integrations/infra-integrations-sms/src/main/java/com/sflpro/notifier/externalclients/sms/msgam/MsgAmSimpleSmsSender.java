package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 5:53 PM
 */
class MsgAmSimpleSmsSender extends AbstractMsgAmSmsSender<SimpleSmsMessage> implements SimpleSmsSender {

    MsgAmSimpleSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator) {
        super(msgAmApiCommunicator);
    }

    @Override
    String bodyFor(final SimpleSmsMessage message) {
        return message.messageBody();
    }

}
