package com.sflpro.notifier.db.entities;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
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
@MappedSuperclass
public abstract class NotificationProperty<T extends Notification> extends AbstractDomainEntityModel {
    private static final long serialVersionUID = 3260222187701442985L;

    /* Properties */
    @Column(name = "property_key", nullable = false)
    private String propertyKey;

    @Column(name = "property_value", nullable = false)
    @Type(type = "text")
    private String propertyValue;

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
        if (!(o instanceof EmailNotificationProperty)) {
            return false;
        }
        final EmailNotificationProperty that = (EmailNotificationProperty) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
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
