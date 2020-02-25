package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:43 AM
 */
public class TemplatedPushNotificationDto extends NotificationDto<PushNotification> {

    private static final long serialVersionUID = -5679998620229746829L;

    public TemplatedPushNotificationDto(final String templateName, final String clientIpAddress, final NotificationProviderType providerType) {
        super(NotificationType.PUSH, null, null, clientIpAddress, templateName, providerType);
    }

    //region Equals, HashCode and ToString

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplatedPushNotificationDto)) {
            return false;
        }
        final TemplatedPushNotificationDto that = (TemplatedPushNotificationDto) o;
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

    //endregion
}
