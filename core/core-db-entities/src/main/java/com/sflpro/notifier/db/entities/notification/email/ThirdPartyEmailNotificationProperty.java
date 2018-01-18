package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
@Entity
@Table(name = "notification_email_third_party_property")
public class ThirdPartyEmailNotificationProperty extends AbstractDomainEntityModel {
    private static final long serialVersionUID = 5563571324365190567L;

    /* Properties */
    @Column(name = "property_key", nullable = false)
    private String propertyKey;

    @Column(name = "property_value")
    @Type(type = "text")
    private String propertyValue;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = false)
    private ThirdPartyEmailNotification thirdPartyEmailNotification;

    /* Constructors */
    public ThirdPartyEmailNotificationProperty() {
        // Default constructor
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

    public ThirdPartyEmailNotification getThirdPartyEmailNotification() {
        return thirdPartyEmailNotification;
    }

    public void setThirdPartyEmailNotification(final ThirdPartyEmailNotification pushNotification) {
        this.thirdPartyEmailNotification = pushNotification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThirdPartyEmailNotificationProperty)) {
            return false;
        }
        final ThirdPartyEmailNotificationProperty that = (ThirdPartyEmailNotificationProperty) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPropertyKey(), that.getPropertyKey());
        builder.append(this.getPropertyValue(), that.getPropertyValue());
        builder.append(getIdOrNull(this.getThirdPartyEmailNotification()), getIdOrNull(that.getThirdPartyEmailNotification()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPropertyKey());
        builder.append(this.getPropertyValue());
        builder.append(getIdOrNull(this.getThirdPartyEmailNotification()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("propertyKey", this.getPropertyKey());
        builder.append("propertyValue", this.getPropertyValue());
        builder.append("pushNotification", getIdOrNull(this.getThirdPartyEmailNotification()));
        return builder.build();
    }
}
