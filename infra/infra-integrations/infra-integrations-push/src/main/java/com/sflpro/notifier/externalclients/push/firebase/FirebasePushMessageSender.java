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
import java.util.function.BiConsumer;
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

    FirebasePushMessageSender(final FirebaseMessaging firebaseMessaging, final AndroidConfig defaultAndroidConfig, final ApnsConfig defaultApnsConfig) {
        this.firebaseMessaging = firebaseMessaging;
        this.platformConfigurationHandlers = Stream.of(
                platformConfigurationHandlerMapping(
                        PlatformType.GCM,
                        (message, builder) -> builder.setAndroidConfig(
                                configForMessage(message,
                                        defaultAndroidConfig))
                ),
                platformConfigurationHandlerMapping(
                        PlatformType.APNS,
                        (message, builder) -> builder.setApnsConfig(
                                configForMessage(message,
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

    private static AndroidConfig configForMessage(final PushMessage message, final AndroidConfig defaultAndroidConfig) {
        logger.debug("Using default android config fro message with subject {}", message.subject());
        return defaultAndroidConfig;
    }

    private static ApnsConfig configForMessage(final PushMessage message, final ApnsConfig defaultApnsConfig) {
        logger.debug("Using default apns config fro message with subject {}", message.subject());
        return defaultApnsConfig;
    }

    private static Pair<PlatformType, BiConsumer<PushMessage, Message.Builder>> platformConfigurationHandlerMapping(final PlatformType platformType, BiConsumer<PushMessage, Message.Builder> platformConfigurationHandler) {
        return Pair.of(
                platformType,
                platformConfigurationHandler
        );
    }
}
