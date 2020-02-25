package com.sflpro.notifier.api.model.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.AbstractApiModel;
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

    //region Properties

    @JsonProperty("propertyKey")
    private String propertyKey;

    @JsonProperty("propertyValue")
    private String propertyValue;

    //endregion

    //region Constants

    public PushNotificationPropertyModel() {
        //default constructor
    }

    public PushNotificationPropertyModel(final String propertyKey, final String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    //endregion

    //region Getters and Setters

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

    //endregion

    //region Equals, HashCode and ToString

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PushNotificationPropertyModel rhs = (PushNotificationPropertyModel) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.propertyKey, rhs.propertyKey)
                .append(this.propertyValue, rhs.propertyValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(propertyKey)
                .append(propertyValue)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("propertyKey", propertyKey)
                .append("propertyValue", propertyValue)
                .toString();
    }

    //endregion
}
