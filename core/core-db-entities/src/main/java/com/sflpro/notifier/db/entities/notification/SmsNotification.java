package com.sflpro.notifier.db.entities.notification;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 12:48 PM
 */
@Entity
@DiscriminatorValue(value = "SMS")
@Table(name = "notification_sms")
public class SmsNotification extends Notification {
    private static final long serialVersionUID = -6341974220564915997L;

    /* Properties */
    @Column(name = "recipient_mobile_number", nullable = false)
    private String recipientMobileNumber;

    /* Constructors */
    public SmsNotification() {
        initializeDefaults();
    }

    public SmsNotification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public String getRecipientMobileNumber() {
        return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(final String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.SMS);
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsNotification)) {
            return false;
        }
        final SmsNotification that = (SmsNotification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getRecipientMobileNumber(), that.getRecipientMobileNumber());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientMobileNumber());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientMobileNumber", this.getRecipientMobileNumber());
        return builder.build();
    }
}
