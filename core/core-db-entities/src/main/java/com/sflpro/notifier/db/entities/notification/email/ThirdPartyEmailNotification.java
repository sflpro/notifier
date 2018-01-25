package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.notification.NotificationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
@Entity
@DiscriminatorValue(value = "EMAIL_THIRD_PARTY")
@Table(name = "notification_email_third_party")
public class ThirdPartyEmailNotification extends EmailNotification {
    private static final long serialVersionUID = 3070320628734635835L;

    @OneToMany(mappedBy = "thirdPartyEmailNotification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ThirdPartyEmailNotificationProperty> properties;

    /* Constructors */
    public ThirdPartyEmailNotification() {
        initializeDefaults();
    }

    public ThirdPartyEmailNotification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public Set<ThirdPartyEmailNotificationProperty> getProperties() {
        return properties;
    }

    public void setProperties(final Set<ThirdPartyEmailNotificationProperty> properties) {
        this.properties = properties;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.EMAIL_THIRD_PARTY);
        this.properties = new LinkedHashSet<>();
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThirdPartyEmailNotification)) {
            return false;
        }
        final ThirdPartyEmailNotification that = (ThirdPartyEmailNotification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        return builder.build();
    }
}
