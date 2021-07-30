package com.sflpro.notifier.services.notification.event.sms;

import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;
import com.sflpro.notifier.services.system.event.model.ApplicationEvent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public class StartSendingNotificationEvent implements ApplicationEvent {

    /* Properties */
    private final Long notificationId;

    private final Map<String, String> secureProperties;

    private final NotificationSendingPriority sendingPriority;

    /* Constructors */
    public StartSendingNotificationEvent(final Long notificationId) {
        Assert.notNull(notificationId, "Notification id should not be null");
        this.notificationId = notificationId;
        this.secureProperties = new HashMap<>();
        this.sendingPriority = NotificationSendingPriority.NORMAL;
    }

    public StartSendingNotificationEvent(final Long notificationId, final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        this.notificationId = notificationId;
        this.secureProperties = secureProperties;
        this.sendingPriority = NotificationSendingPriority.NORMAL;
    }

    public StartSendingNotificationEvent(final Long notificationId, final Map<String, String> secureProperties, final NotificationSendingPriority sendingPriority) {
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        Assert.notNull(sendingPriority, "Sending priority should not be null");
        this.notificationId = notificationId;
        this.secureProperties = secureProperties;
        this.sendingPriority = sendingPriority;
    }

    /* Properties getters and setters */
    public Long getNotificationId() {
        return notificationId;
    }

    public Map<String, String> getSecureProperties() {
        return secureProperties;
    }

    public NotificationSendingPriority getSendingPriority() {
        return sendingPriority;
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
        builder.append(getSecureProperties(), that.getSecureProperties());
        builder.append(getSendingPriority(), that.getSendingPriority());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getNotificationId());
        builder.append(getSecureProperties());
        builder.append(getSendingPriority());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("notificationId", getNotificationId());
        if (this.getSecureProperties() != null) {
            builder.append("secureProperties", getSecureProperties().keySet());
        }
        builder.append("sendingPriority", getSendingPriority());
        return builder.build();
    }
}

