package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 6:00 PM
 */
public class PushNotificationSubscriptionInvalidDeviceUserException extends ServicesRuntimeException {

    private static final long serialVersionUID = -1279686839729319646L;

    /* Properties */
    private final Long subscriptionUserId;

    private final Long deviceUserId;

    private final Long deviceId;

    /* Constructors */
    public PushNotificationSubscriptionInvalidDeviceUserException(final Long subscriptionUserId, final Long deviceId, final Long deviceUserId) {
        super("Push notification subscription belongs to user with id - " + subscriptionUserId + " where as device with id - " + deviceId + " belongs to user with id - " + deviceUserId);
        this.subscriptionUserId = subscriptionUserId;
        this.deviceUserId = deviceUserId;
        this.deviceId = deviceId;
    }

    /* Properties getters and setters */
    public Long getDeviceUserId() {
        return deviceUserId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Long getSubscriptionUserId() {
        return subscriptionUserId;
    }
}
