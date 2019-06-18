package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.sms.SmsSender;
import com.sflpro.notifier.sms.SmsSenderRegistry;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:48 PM
 */
class DefaultSmsSenderProvider implements SmsSenderProvider {

    private final Map<String, SmsSender> registeredSenders;

    DefaultSmsSenderProvider(final List<SmsSenderRegistry> registries) {
        super();
        this.registeredSenders = Collections.unmodifiableMap(
                registries.stream()
                        .collect(Collectors.toMap(SmsSenderRegistry::name, SmsSenderRegistry::sender))
        );
    }

    @Override
    public Optional<SmsSender> lookupSenderFor(final String providerType) {
        Assert.hasText(providerType, "Null or empty text was passed as an argument for parameter 'providerType'.");
        return Optional.ofNullable(registeredSenders.get(providerType));
    }
}
