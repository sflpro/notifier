package com.sflpro.notifier.sms;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:04 PM
 */
public interface SmsMessage {

    String sender();

    String recipientNumber();
}
