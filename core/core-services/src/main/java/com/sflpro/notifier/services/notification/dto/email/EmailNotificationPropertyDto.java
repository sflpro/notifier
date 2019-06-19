package com.sflpro.notifier.services.notification.dto.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:17 PM
 */
public class EmailNotificationPropertyDto extends AbstractDomainEntityModelDto<EmailNotificationProperty> {
    private static final long serialVersionUID = 2424322882785024812L;

    /* Properties */
    private String propertyKey;

    private String propertyValue;

    /* Constructors */
    public EmailNotificationPropertyDto(final String propertyKey, final String propertyValue) {
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    public EmailNotificationPropertyDto() {
        //default constructor
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
    public void updateDomainEntityProperties(final EmailNotificationProperty emailNotificationProperty) {
        Assert.notNull(emailNotificationProperty, "Third party email notification property should not be null");
        emailNotificationProperty.setPropertyKey(getPropertyKey());
        emailNotificationProperty.setPropertyValue(getPropertyValue());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationPropertyDto)) {
            return false;
        }
        final EmailNotificationPropertyDto that = (EmailNotificationPropertyDto) o;
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
