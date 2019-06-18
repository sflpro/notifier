package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.sms.SmsSender;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 4:17 PM
 */
interface SmsSenderProvider {

    Optional<SmsSender> lookupSenderFor(final String providerType);
}
