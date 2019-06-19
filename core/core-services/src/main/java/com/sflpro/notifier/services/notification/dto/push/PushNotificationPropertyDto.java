package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProperty;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 9/10/15
 * Time: 11:32 AM
 */
public class PushNotificationPropertyDto extends AbstractDomainEntityModelDto<PushNotificationProperty> {

    private static final long serialVersionUID = -4441423891899607937L;

    /* Properties */
    private String propertyKey;

    private String propertyValue;

    /* Constructors */
    public PushNotificationPropertyDto(final String propertyKey, final String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    public PushNotificationPropertyDto() {
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

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final PushNotificationProperty pushNotificationProperty) {
        Assert.notNull(pushNotificationProperty, "Push notification property should not be null");
        pushNotificationProperty.setPropertyKey(getPropertyKey());
        pushNotificationProperty.setPropertyValue(getPropertyValue());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationPropertyDto)) {
            return false;
        }
        final PushNotificationPropertyDto that = (PushNotificationPropertyDto) o;
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
