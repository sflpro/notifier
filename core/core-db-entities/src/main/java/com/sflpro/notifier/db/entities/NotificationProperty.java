package com.sflpro.notifier.db.entities;

import com.sflpro.notifier.db.entities.notification.Notification;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/10/19
 * Time: 2:24 PM
 */
@Entity
@Table(name = "notification_property")
public class NotificationProperty extends AbstractDomainEntityModel {
    private static final long serialVersionUID = 3260222187701442985L;

    /* Properties */
    @Column(name = "property_key", nullable = false)
    private String propertyKey;

    @Column(name = "property_value", nullable = false)
    @Type(type = "text")
    private String propertyValue;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = false)
    private Notification notification;

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

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(final Notification notification) {
        this.notification = notification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationProperty)) {
            return false;
        }
        final NotificationProperty that = (NotificationProperty) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(propertyKey, that.propertyKey)
                .append(propertyValue, that.propertyValue)
                .append(getIdOrNull(notification), getIdOrNull(that.notification))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(propertyKey)
                .append(propertyValue)
                .append(notification)
                .toHashCode();
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
