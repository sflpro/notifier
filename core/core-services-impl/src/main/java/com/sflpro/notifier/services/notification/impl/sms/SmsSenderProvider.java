package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.sms.SimpleSmsSender;
import com.sflpro.notifier.sms.TemplatedSmsSender;

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
