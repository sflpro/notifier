package com.sflpro.notifier.api.model.sms.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.notification.response.AbstractCreateNotificationResponse;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 4:57 PM
 */
public class CreateSmsNotificationResponse extends AbstractCreateNotificationResponse {

    private static final long serialVersionUID = 1745285091777329754L;

    /* Properties */
    @JsonProperty("notification")
    private SmsNotificationModel notification;

    /* Constructors */
    public CreateSmsNotificationResponse() {
    }

    public CreateSmsNotificationResponse(final SmsNotificationModel smsNotificationModel) {
        this.notification = smsNotificationModel;
    }

    /* Properties getters and setters */

    public SmsNotificationModel getNotification() {
        return notification;
    }

    public void setNotification(final SmsNotificationModel notification) {
        this.notification = notification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateSmsNotificationResponse)) {
            return false;
        }
        final CreateSmsNotificationResponse that = (CreateSmsNotificationResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getNotification(), that.getNotification());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getNotification());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("notification", this.getNotification());
        return builder.build();
    }
}
