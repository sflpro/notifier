package com.sflpro.notifier.services.notification.model.push;

import com.sflpro.notifier.services.common.model.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 9/10/15
 * Time: 10:52 AM
 */
@Entity
@Table(name = "notification_push_property")
public class PushNotificationProperty extends AbstractDomainEntityModel {

    private static final long serialVersionUID = 5488885427885340245L;

    /* Properties */
    @Column(name = "property_key", nullable = false)
    private String propertyKey;

    @Column(name = "property_value", nullable = false)
    private String propertyValue;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "push_notification_id", nullable = false, unique = false)
    private PushNotification pushNotification;

    /* Constructors */
    public PushNotificationProperty() {
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

    public PushNotification getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(final PushNotification pushNotification) {
        this.pushNotification = pushNotification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationProperty)) {
            return false;
        }
        final PushNotificationProperty that = (PushNotificationProperty) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPropertyKey(), that.getPropertyKey());
        builder.append(this.getPropertyValue(), that.getPropertyValue());
        builder.append(getIdOrNull(this.getPushNotification()), getIdOrNull(that.getPushNotification()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPropertyKey());
        builder.append(this.getPropertyValue());
        builder.append(getIdOrNull(this.getPushNotification()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("propertyKey", this.getPropertyKey());
        builder.append("propertyValue", this.getPropertyValue());
        builder.append("pushNotification", getIdOrNull(this.getPushNotification()));
        return builder.build();
    }
}
