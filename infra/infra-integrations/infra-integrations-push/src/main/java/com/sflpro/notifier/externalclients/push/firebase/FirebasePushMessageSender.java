package com.sflpro.notifier.externalclients.push.firebase;

import com.google.firebase.messaging.*;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/5/19
 * Time: 3:40 PM
 */
class FirebasePushMessageSender implements PushMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(FirebasePushMessageSender.class);

    private final FirebaseMessaging firebaseMessaging;

    private final Map<PlatformType, BiConsumer<PushMessage, Message.Builder>> platformConfigurationHandlers;

    FirebasePushMessageSender(final FirebaseMessaging firebaseMessaging, final Properties defaultAndroidConfig, final Properties defaultApnsConfig) {
        this.firebaseMessaging = firebaseMessaging;
        this.platformConfigurationHandlers = Stream.of(
                platformConfigurationHandlerMapping(
                        PlatformType.GCM,
                        (message, builder) -> builder.setAndroidConfig(
                                configForAndroidMessage(message,
                                        defaultAndroidConfig))
                ),
                platformConfigurationHandlerMapping(
                        PlatformType.APNS,
                        (message, builder) -> builder.setApnsConfig(
                                configForApnsMessage(message,
                                        defaultApnsConfig)
                        )
                )
        ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public PushMessageSendingResult send(final PushMessage message) {
        Assert.notNull(message, "Null was passed as an argument for parameter 'message'.");
        try {
            final Message.Builder builder = Message.builder()
                    .setToken(message.destinationRouteToken())
                    .putAllData(message.properties())
                    .setNotification(new Notification(message.subject(), message.body()));
            platformConfigurationHandler(message.platformType()).ifPresent(handler -> handler.accept(message, builder));
            return PushMessageSendingResult.of(firebaseMessaging.send(builder
                    .build()));
        } catch (final FirebaseMessagingException ex) {
            logger.error("Unable to send message with subject {}.", message.subject());
            throw new MessageSendingFaildException("Filed to send message using firebase cloud messaging.", ex);
        }
    }

    private Optional<BiConsumer<PushMessage, Message.Builder>> platformConfigurationHandler(final PlatformType platformType) {
        return Optional.ofNullable(platformConfigurationHandlers.get(platformType));
    }

    private static AndroidConfig configForAndroidMessage(final PushMessage message, final Properties defaultAndroidConfig) {
        logger.debug("Using default android config fro message with subject {}", message.subject());
        final AndroidConfig.Builder builder = AndroidConfig.builder();
        final Function<String, String> propertyValueFnc = message.properties()::get;
        final Function<String, String> propertyValueFncDefault = defaultAndroidConfig::getProperty;
        valueFor(propertyValueFnc, propertyValueFncDefault, "ttl")
                .map(Long::valueOf)
                .ifPresent(builder::setTtl);
        valueFor(propertyValueFnc, propertyValueFncDefault, "priority")
                .map(AndroidConfig.Priority::valueOf)
                .ifPresent(builder::setPriority);
        valueFor(propertyValueFnc, propertyValueFncDefault, "collapseKey")
                .ifPresent(builder::setCollapseKey);
        valueFor(propertyValueFnc, propertyValueFncDefault, "restrictedPackageName")
                .ifPresent(builder::setRestrictedPackageName);
        return builder.build();
    }

    private static ApnsConfig configForApnsMessage(final PushMessage message, final Properties defaultApnsConfig) {
        logger.debug("Using default apns config fro message with subject {}", message.subject());
        final Function<String, String> propertyValueFnc = message.properties()::get;
        final Function<String, String> propertyValueFncDefault = defaultApnsConfig::getProperty;
        final Aps.Builder apsBuilder = Aps.builder();
        valueFor(propertyValueFnc, propertyValueFncDefault, "badge")
                .map(Integer::valueOf)
                .ifPresent(apsBuilder::setBadge);
        valueFor(propertyValueFnc, propertyValueFncDefault, "category")
                .ifPresent(apsBuilder::setCategory);
        valueFor(propertyValueFnc, propertyValueFncDefault, "sound")
                .ifPresent(apsBuilder::setSound);
        valueFor(propertyValueFnc, propertyValueFncDefault, "alert")
                .ifPresent(apsBuilder::setAlert);
        return ApnsConfig.builder().setAps(apsBuilder.build()).build();
    }

    private static Optional<String> valueFor(final Function<String, String> propertyValueFnc, final Function<String, String> propertyValueFncDefault, final String key) {
        final String value = propertyValueFnc.apply(key);
        if (value == null) {
            return Optional.ofNullable(propertyValueFncDefault.apply(key));
        }
        return Optional.of(value);
    }

    private static Pair<PlatformType, BiConsumer<PushMessage, Message.Builder>> platformConfigurationHandlerMapping(final PlatformType platformType, BiConsumer<PushMessage, Message.Builder> platformConfigurationHandler) {
        return Pair.of(
                platformType,
                platformConfigurationHandler
        );
    }
}
