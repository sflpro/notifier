package com.sflpro.notifier.core.api.internal.model.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:21 PM
 */
public class PushNotificationModel extends NotificationModel {

    private static final long serialVersionUID = 8466440465666629098L;
    /* Properties */
    @JsonProperty("properties")
    private List<PushNotificationPropertyModel> properties;

    @JsonProperty("recipient")
    private PushNotificationRecipientModel recipient;

    /* Constructors */
    public PushNotificationModel() {
        this.properties = new ArrayList<>();
    }

    /* Properties getters and setters */
    public List<PushNotificationPropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(final List<PushNotificationPropertyModel> properties) {
        this.properties = properties;
    }

    public PushNotificationRecipientModel getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipientModel recipient) {
        this.recipient = recipient;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationModel)) {
            return false;
        }
        final PushNotificationModel that = (PushNotificationModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getProperties(), that.getProperties());
        builder.append(this.getRecipient(), that.getRecipient());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getProperties());
        builder.append(this.getRecipient());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("properties", this.getProperties());
        builder.append("recipient", this.getRecipient());
        return builder.build();
    }
}
