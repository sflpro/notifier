package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 1:54 PM
 */
public class PushNotificationRecipientSearchFilter {

    /* Properties */
    private Long subscriptionId;

    private PushNotificationRecipientStatus status;

    private String destinationRouteToken;

    private PushNotificationProviderType providerType;

    private DeviceOperatingSystemType deviceOperatingSystemType;

    private String applicationType;

    private String deviceUuId;

    /* Constructors */
    public PushNotificationRecipientSearchFilter() {
        super();
    }

    /* Properties getters and setters */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public PushNotificationRecipientSearchFilter setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public PushNotificationRecipientStatus getStatus() {
        return status;
    }

    public PushNotificationRecipientSearchFilter setStatus(final PushNotificationRecipientStatus status) {
        this.status = status;
        return this;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }

    public PushNotificationRecipientSearchFilter setDestinationRouteToken(final String destinationRouteToken) {
        this.destinationRouteToken = destinationRouteToken;
        return this;
    }

    public PushNotificationProviderType getProviderType() {
        return providerType;
    }

    public PushNotificationRecipientSearchFilter setProviderType(final PushNotificationProviderType providerType) {
        this.providerType = providerType;
        return this;
    }

    public DeviceOperatingSystemType getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public PushNotificationRecipientSearchFilter setDeviceOperatingSystemType(final DeviceOperatingSystemType deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
        return this;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public PushNotificationRecipientSearchFilter setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public String getDeviceUuId() {
        return deviceUuId;
    }

    public PushNotificationRecipientSearchFilter setDeviceUuId(final String deviceUuId) {
        this.deviceUuId = deviceUuId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PushNotificationRecipientSearchFilter that = (PushNotificationRecipientSearchFilter) o;

        return new EqualsBuilder()
                .append(subscriptionId, that.subscriptionId)
                .append(status, that.status)
                .append(destinationRouteToken, that.destinationRouteToken)
                .append(providerType, that.providerType)
                .append(deviceOperatingSystemType, that.deviceOperatingSystemType)
                .append(applicationType, that.applicationType)
                .append(deviceUuId, that.deviceUuId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(subscriptionId)
                .append(status)
                .append(destinationRouteToken)
                .append(providerType)
                .append(deviceOperatingSystemType)
                .append(applicationType)
                .append(deviceUuId)
                .toHashCode();
    }
}
