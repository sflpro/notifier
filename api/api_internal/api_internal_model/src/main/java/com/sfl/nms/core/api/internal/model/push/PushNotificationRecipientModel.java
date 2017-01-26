package com.sfl.nms.core.api.internal.model.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.nms.core.api.internal.model.common.AbstractApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:30 PM
 */
public class PushNotificationRecipientModel extends AbstractApiModel {

    private static final long serialVersionUID = -4636585738665933255L;

    /* Properties */
    @JsonProperty("deviceOperatingSystemType")
    private String deviceOperatingSystemType;

    @JsonProperty("applicationType")
    private String applicationType;

    /* Constructors */
    public PushNotificationRecipientModel() {
    }

    /* Properties getters and setters */
    public String getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public void setDeviceOperatingSystemType(final String deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationRecipientModel)) {
            return false;
        }
        final PushNotificationRecipientModel that = (PushNotificationRecipientModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getDeviceOperatingSystemType(), that.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType(), that.getApplicationType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("deviceOperatingSystemType", this.getDeviceOperatingSystemType());
        builder.append("applicationType", this.getApplicationType());
        return builder.build();
    }
}
