package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipientDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 4:00 PM
 */
@Repository
public interface PushNotificationRecipientDeviceRepository extends JpaRepository<PushNotificationRecipientDevice, Long> {


    /**
     * Finds push notification recipient device by recipient and user device
     *
     * @param recipient
     * @param device
     * @return
     */
    PushNotificationRecipientDevice findByRecipientAndDevice(@Nonnull final PushNotificationRecipient recipient, @Nonnull final UserDevice device);
}
