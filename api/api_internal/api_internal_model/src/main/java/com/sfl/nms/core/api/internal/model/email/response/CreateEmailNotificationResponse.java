package com.sfl.nms.core.api.internal.model.email.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.nms.core.api.internal.model.email.EmailNotificationModel;
import com.sfl.nms.core.api.internal.model.notification.response.AbstractCreateNotificationResponse;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:43 PM
 */
public class CreateEmailNotificationResponse extends AbstractCreateNotificationResponse {

    private static final long serialVersionUID = -4705186873875336165L;

    /* Properties */
    @JsonProperty("notification")
    private EmailNotificationModel notification;

    /* Constructors */
    public CreateEmailNotificationResponse() {
    }

    public CreateEmailNotificationResponse(final EmailNotificationModel notification) {
        this.notification = notification;
    }

    /* Properties getters and setters */
    public EmailNotificationModel getNotification() {
        return notification;
    }

    public void setNotification(final EmailNotificationModel notification) {
        this.notification = notification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateEmailNotificationResponse)) {
            return false;
        }
        final CreateEmailNotificationResponse that = (CreateEmailNotificationResponse) o;
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
