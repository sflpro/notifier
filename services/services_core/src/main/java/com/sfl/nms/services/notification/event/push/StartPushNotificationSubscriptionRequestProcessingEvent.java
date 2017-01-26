package com.sfl.nms.services.notification.event.push;

import com.sfl.nms.services.system.event.model.ApplicationEvent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public class StartPushNotificationSubscriptionRequestProcessingEvent implements ApplicationEvent {

    /* Properties */
    private final Long pushNotificationSubscriptionRequestId;

    /* Constructors */
    public StartPushNotificationSubscriptionRequestProcessingEvent(final Long pushNotificationSubscriptionRequestId) {
        Assert.notNull(pushNotificationSubscriptionRequestId, "Sms notification id should not be null");
        this.pushNotificationSubscriptionRequestId = pushNotificationSubscriptionRequestId;
    }

    /* Properties getters and setters */
    public Long getPushNotificationSubscriptionRequestId() {
        return pushNotificationSubscriptionRequestId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StartPushNotificationSubscriptionRequestProcessingEvent)) {
            return false;
        }
        final StartPushNotificationSubscriptionRequestProcessingEvent that = (StartPushNotificationSubscriptionRequestProcessingEvent) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getPushNotificationSubscriptionRequestId(), that.getPushNotificationSubscriptionRequestId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getPushNotificationSubscriptionRequestId());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("pushNotificationSubscriptionRequestId", getPushNotificationSubscriptionRequestId());
        return builder.build();
    }
}

