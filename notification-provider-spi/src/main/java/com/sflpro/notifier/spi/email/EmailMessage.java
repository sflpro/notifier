package com.sflpro.notifier.spi.email;

import java.util.Set;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:38 AM
 */
public interface EmailMessage {

    String from();

    String to();

    Set<String> replyTo();

    Set<SpiEmailNotificationFileAttachment> fileAttachments();

}
