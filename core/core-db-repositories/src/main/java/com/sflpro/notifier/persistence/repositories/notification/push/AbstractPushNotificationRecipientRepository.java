package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:08 PM
 */
public interface AbstractPushNotificationRecipientRepository<T extends PushNotificationRecipient> extends JpaRepository<T, Long> {


    /**
     * Finds push notification recipient by type, subscription and recipient route token
     *
     * @param type
     * @param subscription
     * @param destinationRouteToken
     * @param applicationType
     * @return pushNotificationRecipient
     */
    T findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(@Nonnull final PushNotificationProviderType type, @Nonnull final PushNotificationSubscription subscription, @Nonnull final String destinationRouteToken, final String applicationType);
}
