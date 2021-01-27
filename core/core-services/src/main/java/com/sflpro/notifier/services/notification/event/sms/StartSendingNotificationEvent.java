package com.sflpro.notifier.services.notification.event.sms;

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

    /* Constructors */
    public StartSendingNotificationEvent(final Long notificationId) {
        Assert.notNull(notificationId, "Notification id should not be null");
        this.notificationId = notificationId;
        this.secureProperties = new HashMap<>();
    }

    public StartSendingNotificationEvent(final Long notificationId, final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        this.notificationId = notificationId;
        this.secureProperties = secureProperties;
    }

    /* Properties getters and setters */
    public Long getNotificationId() {
        return notificationId;
    }

    public Map<String, String> getSecureProperties() {
        return secureProperties;
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
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getNotificationId());
        builder.append(getSecureProperties());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("notificationId", getNotificationId());
        if (this.getSecureProperties() != null) {
            builder.append("secureProperties", getSecureProperties().keySet());
        }
        return builder.build();
    }
}

