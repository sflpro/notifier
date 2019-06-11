package com.sflpro.notifier.services.notification.dto;

import com.sflpro.notifier.db.entities.NotificationProperty;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/10/19
 * Time: 2:44 PM
 */
public abstract class AbstractNotificationPropertyModelDto<T extends NotificationProperty> extends AbstractDomainEntityModelDto<T> {
    private static final long serialVersionUID = -6041671930476167207L;

    /* Properties */
    private String propertyKey;

    private String propertyValue;

    /* Constructors */
    public AbstractNotificationPropertyModelDto() {
    }

    public AbstractNotificationPropertyModelDto(final String propertyKey, final String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

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

    /* Public methods */
    @Override
    public void updateDomainEntityProperties(final NotificationProperty notificationProperty) {
        Assert.notNull(notificationProperty, "Third party notification property should not be null");
        notificationProperty.setPropertyKey(getPropertyKey());
        notificationProperty.setPropertyValue(getPropertyValue());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractNotificationPropertyModelDto)) {
            return false;
        }
        final AbstractNotificationPropertyModelDto<?> that = (AbstractNotificationPropertyModelDto<?>) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(propertyKey, that.propertyKey)
                .append(propertyValue, that.propertyValue)
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
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("propertyKey", this.getPropertyKey());
        builder.append("propertyValue", this.getPropertyValue());
        return builder.build();
    }
}
