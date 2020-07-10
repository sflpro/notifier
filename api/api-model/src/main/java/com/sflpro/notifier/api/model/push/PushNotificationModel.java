package com.sflpro.notifier.api.model.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:21 PM
 */
public class PushNotificationModel extends NotificationModel {

    private static final long serialVersionUID = 8466440465666629098L;

    //region Properties

    @JsonProperty("properties")
    private Map<String, String> properties;

    @JsonProperty("recipient")
    private PushNotificationRecipientModel recipient;

    //endregion

    //region Constants

    public PushNotificationModel() {
        this.properties = new HashMap<>();
    }

    //endregion

    //region Getters and Setters

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public PushNotificationRecipientModel getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipientModel recipient) {
        this.recipient = recipient;
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
        PushNotificationModel rhs = (PushNotificationModel) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.properties, rhs.properties)
                .append(this.recipient, rhs.recipient)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(properties)
                .append(recipient)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("properties", properties)
                .append("recipient", recipient)
                .toString();
    }

    //endregion
}
