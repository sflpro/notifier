package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.sms.SimpleSmsSender;
import com.sflpro.notifier.sms.SimpleSmsSenderRegistry;
import com.sflpro.notifier.sms.TemplatedSmsSender;
import com.sflpro.notifier.sms.TemplatedSmsSenderRegistry;
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

    private final Map<String, SimpleSmsSender> registeredSimpleSmsSenders;
    private final Map<String, TemplatedSmsSender> registeredTemplatedSmsSender;

    DefaultSmsSenderProvider(final List<SimpleSmsSenderRegistry> simpleSmsSenderRegistries,
                             final List<TemplatedSmsSenderRegistry> templatedSmsSenderRegistries) {
        super();
        this.registeredSimpleSmsSenders = Collections.unmodifiableMap(
                simpleSmsSenderRegistries.stream()
                        .collect(Collectors.toMap(SimpleSmsSenderRegistry::name, SimpleSmsSenderRegistry::sender))
        );

        this.registeredTemplatedSmsSender = Collections.unmodifiableMap(
                templatedSmsSenderRegistries.stream()
                        .collect(Collectors.toMap(TemplatedSmsSenderRegistry::name, TemplatedSmsSenderRegistry::sender))
        );
    }

    @Override
    public Optional<SimpleSmsSender> lookupSimpleSmsMessageSenderFor(final String providerType) {
        Assert.hasText(providerType, "Null or empty text was passed as an argument for parameter 'providerType'.");
        return Optional.ofNullable(registeredSimpleSmsSenders.get(providerType));
    }

    @Override
    public Optional<TemplatedSmsSender> lookupTemplatedSmsMessageSenderFor(final String providerType) {
        Assert.hasText(providerType, "Null or empty text was passed as an argument for parameter 'providerType'.");
        return Optional.ofNullable(registeredTemplatedSmsSender.get(providerType));
    }
}
