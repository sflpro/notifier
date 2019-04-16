package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotification;

import javax.annotation.Nonnull;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/16/19
 * Time: 8:24 PM
 */
public interface EmailNotificationProviderProcessor {

    boolean processEmailNotification(@Nonnull final EmailNotification emailNotification);
}
