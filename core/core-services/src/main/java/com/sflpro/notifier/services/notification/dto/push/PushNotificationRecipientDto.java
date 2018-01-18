package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import com.sflpro.notifier.services.device.model.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.notification.model.push.PushNotificationProviderType;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 12:59 PM
 */
public abstract class PushNotificationRecipientDto<T extends PushNotificationRecipient> extends AbstractDomainEntityModelDto<T> {

    private static final long serialVersionUID = -2740450685179429085L;

    /* Properties */
    private final PushNotificationProviderType type;

    private String destinationRouteToken;

    private DeviceOperatingSystemType deviceOperatingSystemType;

    private String applicationType;


    /* Constructors */
    public PushNotificationRecipientDto(final PushNotificationProviderType type) {
        this.type = type;
    }

    public PushNotificationRecipientDto(final PushNotificationProviderType type, final String destinationRouteToken, final DeviceOperatingSystemType deviceOperatingSystemType, final String applicationType) {
        this(type);
        this.destinationRouteToken = destinationRouteToken;
        this.deviceOperatingSystemType = deviceOperatingSystemType;
        this.applicationType = applicationType;
    }

    /* Properties getters and setters */
    public PushNotificationProviderType getType() {
        return type;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }

    public void setDestinationRouteToken(final String destinationRouteToken) {
        this.destinationRouteToken = destinationRouteToken;
    }

    public DeviceOperatingSystemType getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public void setDeviceOperatingSystemType(final DeviceOperatingSystemType deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final T recipient) {
        Assert.notNull("Push notification recipient should not be null");
        recipient.setDestinationRouteToken(getDestinationRouteToken());
        recipient.setDeviceOperatingSystemType(getDeviceOperatingSystemType());
        recipient.setApplicationType(getApplicationType());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationRecipientDto)) {
            return false;
        }
        final PushNotificationRecipientDto that = (PushNotificationRecipientDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getType(), that.getType());
        builder.append(this.getDestinationRouteToken(), that.getDestinationRouteToken());
        builder.append(this.getDeviceOperatingSystemType(), that.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType(), that.getApplicationType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getType());
        builder.append(this.getDestinationRouteToken());
        builder.append(this.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("type", this.getType());
        builder.append("destinationRouteToken", this.getDestinationRouteToken());
        builder.append("deviceOperatingSystemType", this.getDeviceOperatingSystemType());
        builder.append("applicationType", this.getApplicationType());
        return builder.build();
    }
}
