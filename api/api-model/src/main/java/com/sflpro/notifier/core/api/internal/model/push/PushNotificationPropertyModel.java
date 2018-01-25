package com.sflpro.notifier.core.api.internal.model.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.common.AbstractApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:24 PM
 */
public class PushNotificationPropertyModel extends AbstractApiModel {

    private static final long serialVersionUID = -3654804345700318929L;

    /* Properties */
    @JsonProperty("propertyKey")
    private String propertyKey;

    @JsonProperty("propertyValue")
    private String propertyValue;

    /* Constructors */
    public PushNotificationPropertyModel() {
        //default constructor
    }

    public PushNotificationPropertyModel(final String propertyKey, final String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    /* Properties getters and setters */
    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(final String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(final String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationPropertyModel)) {
            return false;
        }
        final PushNotificationPropertyModel that = (PushNotificationPropertyModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getPropertyKey(), that.getPropertyKey());
        builder.append(this.getPropertyValue(), that.getPropertyValue());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPropertyKey());
        builder.append(this.getPropertyValue());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("propertyKey", this.getPropertyKey());
        builder.append("propertyValue", this.getPropertyValue());
        return builder.build();
    }
}
