package com.sflpro.notifier.services.notification.impl.email;


import com.sflpro.notifier.spi.email.SimpleEmailSender;
import com.sflpro.notifier.spi.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import com.sflpro.notifier.spi.email.TemplatedEmailSenderRegistry;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:57 AM
 */

class DefaultEmailSenderProvider implements EmailSenderProvider {

    private final Map<String, SimpleEmailSender> registeredSimpleEmailSenders;
    private final Map<String, TemplatedEmailSender> registeredTemplatedEmailSenders;

    DefaultEmailSenderProvider(final List<SimpleEmailSenderRegistry> simpleEmailSenderRegistries,
                               final List<TemplatedEmailSenderRegistry> templatedEmailSenderRegistries) {
        this.registeredSimpleEmailSenders = simpleEmailSenderRegistries.stream()
                .collect(Collectors.toMap(SimpleEmailSenderRegistry::name, SimpleEmailSenderRegistry::sender));
        this.registeredTemplatedEmailSenders = templatedEmailSenderRegistries.stream()
                .collect(Collectors.toMap(TemplatedEmailSenderRegistry::name, TemplatedEmailSenderRegistry::sender));
    }

    @Override
    public Optional<SimpleEmailSender> lookupSimpleEmailSenderFor(final String providerType) {
        assertValidProviderTypeArgument(providerType);
        return Optional.ofNullable(registeredSimpleEmailSenders.get(providerType));
    }

    @Override
    public Optional<TemplatedEmailSender> lookupTemplatedEmailSenderFor(final String providerType) {
        assertValidProviderTypeArgument(providerType);
        return Optional.ofNullable(registeredTemplatedEmailSenders.get(providerType));
    }

    private static void assertValidProviderTypeArgument(final String providerType) {
        Assert.hasText(providerType, "Null or empty text was passed as an argument for parameter 'providerType'.");
    }
}
