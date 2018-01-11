package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 6:00 PM
 */
public class PushNotificationRecipientInvalidDeviceUserException extends ServicesRuntimeException {

    private static final long serialVersionUID = -1279686839729319646L;

    /* Properties */
    private final Long pushNotificationRecipientId;

    private final Long pushNotificationRecipientUserId;

    private final Long deviceUserId;

    private final Long deviceId;

    /* Constructors */
    public PushNotificationRecipientInvalidDeviceUserException(final Long pushNotificationRecipientId, final Long pushNotificationRecipientUserId, final Long deviceId, final Long deviceUserId) {
        super("Push notification recipient with id - " + pushNotificationRecipientId + " belongs to user with id - " + pushNotificationRecipientUserId + " where as device with id - " + deviceId + " belongs to user with id - " + deviceUserId);
        this.pushNotificationRecipientUserId = pushNotificationRecipientUserId;
        this.deviceUserId = deviceUserId;
        this.pushNotificationRecipientId = pushNotificationRecipientId;
        this.deviceId = deviceId;
    }

    /* Properties getters and setters */
    public Long getPushNotificationRecipientId() {
        return pushNotificationRecipientId;
    }

    public Long getPushNotificationRecipientUserId() {
        return pushNotificationRecipientUserId;
    }

    public Long getDeviceUserId() {
        return deviceUserId;
    }

    public Long getDeviceId() {
        return deviceId;
    }
}
