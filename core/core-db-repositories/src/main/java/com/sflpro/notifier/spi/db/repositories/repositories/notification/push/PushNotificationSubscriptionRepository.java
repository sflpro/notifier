package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:19 AM
 */
@Repository
public interface PushNotificationSubscriptionRepository extends JpaRepository<PushNotificationSubscription, Long> {

    /**
     * Finds push notification subscription by user
     *
     * @param user
     * @return pushNotificationSubscription
     */
    PushNotificationSubscription findByUser(@Nonnull final User user);
}
