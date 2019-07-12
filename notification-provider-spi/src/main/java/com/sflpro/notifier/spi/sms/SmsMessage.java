package com.sflpro.notifier.spi.sms;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:04 PM
 */
public interface SmsMessage {

    String sender();

    String recipientNumber();

    long internalId();

    default String contentType(){
        return "text/plain";
    }
}
