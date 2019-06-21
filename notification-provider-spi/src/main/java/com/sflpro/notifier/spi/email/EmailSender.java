package com.sflpro.notifier.spi.email;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:23 AM
 */
public interface EmailSender<M extends EmailMessage> {

    void send(final M message);
}
