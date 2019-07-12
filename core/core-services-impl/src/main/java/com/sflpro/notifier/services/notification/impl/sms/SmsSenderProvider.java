package com.sflpro.notifier.services.notification.impl.sms;


import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 4:17 PM
 */
interface SmsSenderProvider {

    Optional<SimpleSmsSender> lookupSimpleSmsMessageSenderFor(final String providerType);

    Optional<TemplatedSmsSender> lookupTemplatedSmsMessageSenderFor(final String providerType);
}
