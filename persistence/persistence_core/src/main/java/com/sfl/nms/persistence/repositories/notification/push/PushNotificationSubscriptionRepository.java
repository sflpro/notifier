package com.sfl.nms.persistence.repositories.notification.push;

import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.user.model.User;
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
