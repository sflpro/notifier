package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageServiceRegistry;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 4:00 PM
 */
@Component
class DefaultPushMessageServiceProvider implements PushMessageServiceProvider {

    private final Map<PushNotificationProviderType, PushMessageSender> pushMessageSenders;
    private final Map<PushNotificationProviderType, PushMessageSubscriber> pushMessageSubscribers;

    @SuppressWarnings("unchecked")
    DefaultPushMessageServiceProvider(final List<PushMessageServiceRegistry> registries) {
        this.pushMessageSenders = Collections.unmodifiableMap(registries.stream()
                .collect(Collectors.toMap(
                        DefaultPushMessageServiceProvider::providerType,
                        PushMessageServiceRegistry::sender,
                        takeLastFunction(),
                        () -> new EnumMap(PushNotificationProviderType.class)))
        );
        this.pushMessageSubscribers = Collections.unmodifiableMap(registries.stream()
                .collect(Collectors.toMap(
                        DefaultPushMessageServiceProvider::providerType,
                        PushMessageServiceRegistry::subscriber,
                        takeLastFunction(),
                        () -> new EnumMap(PushNotificationProviderType.class)))
        );
    }

    @Override
    public Optional<PushMessageSender> lookupPushMessageSender(final PushNotificationProviderType providerType) {
        assertValidProviderType(providerType);
        return Optional.ofNullable(pushMessageSenders.get(providerType));
    }

    @Override
    public Optional<PushMessageSubscriber> lookupPushMessageSubscriber(final PushNotificationProviderType providerType) {
        assertValidProviderType(providerType);
        return Optional.ofNullable(pushMessageSubscribers.get(providerType));
    }

    private static PushNotificationProviderType providerType(final PushMessageServiceRegistry registry) {
        return PushNotificationProviderType.valueOf(registry.name().toUpperCase());
    }

    private static <T> BinaryOperator<T> takeLastFunction() {
        return DefaultPushMessageServiceProvider::takeLast;
    }

    private static <T> T takeLast(@SuppressWarnings("unused") final T first, final T second) {
        return second;
    }

    private void assertValidProviderType(final PushNotificationProviderType providerType) {
        Assert.notNull(providerType, "Null was provided as an argument for parameter 'providerType'.");
    }
}

