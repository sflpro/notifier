package com.sflpro.notifier.sms;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:45 PM
 */
public interface SmsSender {

    SmsMessageSendingResult send(final SmsMessage smsMessage);

}
