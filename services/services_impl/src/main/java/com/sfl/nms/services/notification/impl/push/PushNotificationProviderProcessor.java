package com.sfl.nms.services.notification.impl.push;

import com.sfl.nms.services.notification.model.push.PushNotification;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 3:36 PM
 */
public interface PushNotificationProviderProcessor {

    /**
     * Processes push notification specific implementation
     *
     * @param pushNotification
     * @return providerExternalUuId
     *
     */
    String processPushNotification(@Nonnull final PushNotification pushNotification);
}
