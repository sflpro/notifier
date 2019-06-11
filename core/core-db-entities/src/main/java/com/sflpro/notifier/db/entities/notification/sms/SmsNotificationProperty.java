package com.sflpro.notifier.db.entities.notification.sms;

import com.sflpro.notifier.db.entities.NotificationProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
@Entity
@Table(name = "notification_sms_property")
public class SmsNotificationProperty extends NotificationProperty {
    private static final long serialVersionUID = 5563571324365190567L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = false)
    private SmsNotification smsNotification;

    /* Properties getters and setters */
    public SmsNotification getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(final SmsNotification smsNotification) {
        this.smsNotification = smsNotification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsNotificationProperty)) {
            return false;
        }
        final SmsNotificationProperty that = (SmsNotificationProperty) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getIdOrNull(this.getSmsNotification()), getIdOrNull(that.getSmsNotification()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getIdOrNull(this.getSmsNotification()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("smsNotification", getIdOrNull(this.getSmsNotification()));
        return builder.build();
    }
}
