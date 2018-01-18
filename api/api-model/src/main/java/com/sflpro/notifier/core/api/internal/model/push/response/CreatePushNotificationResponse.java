package com.sflpro.notifier.core.api.internal.model.push.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.notification.response.AbstractCreateNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:37 PM
 */
public class CreatePushNotificationResponse extends AbstractCreateNotificationResponse {

    private static final long serialVersionUID = 6320282890547985657L;

    /* Properties */
    @JsonProperty("notifications")
    private List<PushNotificationModel> notifications;

    /* Constructors */
    public CreatePushNotificationResponse() {
        this.notifications = new ArrayList<>();
    }

    public CreatePushNotificationResponse(List<PushNotificationModel> notifications) {
        this.notifications = notifications;
    }

    /* Properties getters and setters */
    public List<PushNotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(final List<PushNotificationModel> notifications) {
        this.notifications = notifications;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatePushNotificationResponse)) {
            return false;
        }
        final CreatePushNotificationResponse that = (CreatePushNotificationResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getNotifications(), that.getNotifications());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getNotifications());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("notifications", this.getNotifications());
        return builder.build();
    }
}
