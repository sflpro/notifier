package com.sflpro.notifier.spi.email;

import java.util.List;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:38 AM
 */
public interface EmailMessage {

    String from();

    String to();

    List<SpiEmailNotificationFileAttachment> fileAttachments();

}
