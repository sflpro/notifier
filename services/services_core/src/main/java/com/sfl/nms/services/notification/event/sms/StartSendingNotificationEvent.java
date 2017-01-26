package com.sfl.nms.services.notification.event.sms;

import com.sfl.nms.services.system.event.model.ApplicationEvent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public class StartSendingNotificationEvent implements ApplicationEvent {

    /* Properties */
    private final Long notificationId;

    /* Constructors */
    public StartSendingNotificationEvent(final Long notificationId) {
        Assert.notNull(notificationId, "Notification id should not be null");
        this.notificationId = notificationId;
    }

    /* Properties getters and setters */
    public Long getNotificationId() {
        return notificationId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StartSendingNotificationEvent)) {
            return false;
        }
        final StartSendingNotificationEvent that = (StartSendingNotificationEvent) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getNotificationId(), that.getNotificationId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getNotificationId());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("notificationId", getNotificationId());
        return builder.build();
    }
}

