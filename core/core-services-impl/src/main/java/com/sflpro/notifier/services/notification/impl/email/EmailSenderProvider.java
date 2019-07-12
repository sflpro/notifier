package com.sflpro.notifier.services.notification.impl.email;


import com.sflpro.notifier.spi.email.SimpleEmailSender;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:24 AM
 */
interface EmailSenderProvider {

    Optional<SimpleEmailSender> lookupSimpleEmailSenderFor(final String providerType);

    Optional<TemplatedEmailSender> lookupTemplatedEmailSenderFor(final String providerType);
}
