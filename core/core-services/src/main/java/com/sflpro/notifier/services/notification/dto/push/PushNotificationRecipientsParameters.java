package com.sflpro.notifier.services.notification.dto.push;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 06/08/2022
 * Time: 15:33
 */
public class PushNotificationRecipientsParameters implements Serializable {

    private final Long userId;
    private final String deviceUuId;

    public PushNotificationRecipientsParameters(final Long userId, final String deviceUuId) {
        this.userId = userId;
        this.deviceUuId = deviceUuId;
    }

    public PushNotificationRecipientsParameters(final Long userId) {
        this(userId, null);
    }

    public Long getUserId() {
        return userId;
    }

    public String getDeviceUuId() {
        return deviceUuId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final PushNotificationRecipientsParameters that = (PushNotificationRecipientsParameters) o;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(deviceUuId, that.deviceUuId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(userId)
                .append(deviceUuId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("userId", userId)
                .append("deviceUuId", deviceUuId)
                .toString();
    }
}
