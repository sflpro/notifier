package com.sflpro.notifier.api.model.sms;

import com.sflpro.notifier.api.model.notification.NotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 2:53 PM
 */
public class SmsNotificationModel extends NotificationModel {

    private static final long serialVersionUID = -4742052539749023959L;

    /* Properties */
    private String recipientNumber;

    /* Constructors */
    public SmsNotificationModel() {
        //default constructor
    }

    /* Properties getters and setters */
    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(final String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsNotificationModel)) {
            return false;
        }
        final SmsNotificationModel that = (SmsNotificationModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getRecipientNumber(), that.getRecipientNumber());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientNumber());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientNumber", this.getRecipientNumber());
        return builder.build();
    }
}
