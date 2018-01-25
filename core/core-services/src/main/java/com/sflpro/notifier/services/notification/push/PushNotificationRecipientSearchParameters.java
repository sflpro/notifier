package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 1:54 PM
 */
public class PushNotificationRecipientSearchParameters implements Serializable {

    private static final long serialVersionUID = 2906139386476772398L;

    /* Properties */
    private Long subscriptionId;

    private PushNotificationRecipientStatus status;

    private String destinationRouteToken;

    private PushNotificationProviderType providerType;

    private DeviceOperatingSystemType deviceOperatingSystemType;

    private String applicationType;

    /* Constructors */
    public PushNotificationRecipientSearchParameters() {
        super();
    }

    /* Properties getters and setters */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public PushNotificationRecipientSearchParameters setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public PushNotificationRecipientStatus getStatus() {
        return status;
    }

    public PushNotificationRecipientSearchParameters setStatus(final PushNotificationRecipientStatus status) {
        this.status = status;
        return this;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }

    public PushNotificationRecipientSearchParameters setDestinationRouteToken(final String destinationRouteToken) {
        this.destinationRouteToken = destinationRouteToken;
        return this;
    }

    public PushNotificationProviderType getProviderType() {
        return providerType;
    }

    public PushNotificationRecipientSearchParameters setProviderType(final PushNotificationProviderType providerType) {
        this.providerType = providerType;
        return this;
    }

    public DeviceOperatingSystemType getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public PushNotificationRecipientSearchParameters setDeviceOperatingSystemType(final DeviceOperatingSystemType deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
        return this;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public PushNotificationRecipientSearchParameters setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationRecipientSearchParameters)) {
            return false;
        }
        final PushNotificationRecipientSearchParameters that = (PushNotificationRecipientSearchParameters) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getStatus(), that.getStatus());
        builder.append(this.getDestinationRouteToken(), that.getDestinationRouteToken());
        builder.append(this.getProviderType(), that.getProviderType());
        builder.append(this.getSubscriptionId(), that.getSubscriptionId());
        builder.append(this.getDeviceOperatingSystemType(), that.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType(), that.getApplicationType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getStatus());
        builder.append(this.getDestinationRouteToken());
        builder.append(this.getProviderType());
        builder.append(this.getSubscriptionId());
        builder.append(this.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("status", this.getStatus());
        builder.append("destinationRouteToken", this.getDestinationRouteToken());
        builder.append("providerType", this.getProviderType());
        builder.append("subscriptionId", this.getSubscriptionId());
        builder.append("deviceOperatingSystemType", this.getDeviceOperatingSystemType());
        builder.append("applicationType", this.getApplicationType());
        return builder.build();
    }
}
