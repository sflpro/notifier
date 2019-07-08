package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 12:03 PM
 */
public interface PushMessageServiceProvider {

    Optional<PushMessageSender> lookupPushMessageSender(final PushNotificationProviderType providerType);

    Optional<PushMessageSubscriber> lookupPushMessageSubscriber(final PushNotificationProviderType providerType);
}
