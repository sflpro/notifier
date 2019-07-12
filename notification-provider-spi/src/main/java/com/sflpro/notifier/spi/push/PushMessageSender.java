package com.sflpro.notifier.spi.push;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:42 AM
 */
public interface PushMessageSender {

    PushMessageSendingResult send(final PushMessage message);
}
