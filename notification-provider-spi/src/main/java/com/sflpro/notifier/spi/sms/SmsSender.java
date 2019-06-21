package com.sflpro.notifier.spi.sms;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:15 PM
 */
public interface SmsSender<M extends SmsMessage> {

    SmsMessageSendingResult send(final M message);
}
