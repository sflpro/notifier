package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.model.NotificationType;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:43 AM
 */
public class PushNotificationDto extends NotificationDto<PushNotification> {

    private static final long serialVersionUID = -5679998620229746829L;

    /* Constructors */
    public PushNotificationDto() {
        super(NotificationType.PUSH);
    }

    public PushNotificationDto(final String content, final String subject, final String clientIpAddress) {
        super(NotificationType.PUSH, content, subject, clientIpAddress);
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationDto)) {
            return false;
        }
        final PushNotificationDto that = (PushNotificationDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        return builder.build();
    }
}
