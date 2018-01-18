package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscription;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:21 AM
 */
public class PushNotificationSubscriptionDto extends AbstractDomainEntityModelDto<PushNotificationSubscription> {

    private static final long serialVersionUID = -4534533652804275009L;

    /* Constructors */
    public PushNotificationSubscriptionDto() {
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final PushNotificationSubscription subscription) {
        Assert.notNull(subscription, "Push notification subscription should not be null");
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscriptionDto)) {
            return false;
        }
        final PushNotificationSubscriptionDto that = (PushNotificationSubscriptionDto) o;
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
