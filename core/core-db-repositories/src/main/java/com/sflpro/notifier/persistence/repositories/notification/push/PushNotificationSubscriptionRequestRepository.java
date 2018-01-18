package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:17 AM
 */
@Repository
public interface PushNotificationSubscriptionRequestRepository extends JpaRepository<PushNotificationSubscriptionRequest, Long> {


    /**
     * Finds push notification subscription by uuid
     *
     * @param uuId
     * @return
     */
    PushNotificationSubscriptionRequest findByUuId(@Nonnull final String uuId);
}
