package com.sflpro.notifier.queue.amqp.model.notification.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.queue.amqp.model.AbstractRPCTransferModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public class PushNotificationSubscriptionRequestRPCTransferModel extends AbstractRPCTransferModel {
    private static final long serialVersionUID = -5215994146311541084L;

    /* Properties */
    @JsonProperty(value = "subscriptionRequestId", required = true)
    private Long subscriptionRequestId;

    /* Constructors */
    public PushNotificationSubscriptionRequestRPCTransferModel(final Long subscriptionRequestId) {
        this.subscriptionRequestId = subscriptionRequestId;
    }

    public PushNotificationSubscriptionRequestRPCTransferModel() {
    }

    /* Properties getters and setters */
    public Long getSubscriptionRequestId() {
        return subscriptionRequestId;
    }

    public void setSubscriptionRequestId(final Long subscriptionRequestId) {
        this.subscriptionRequestId = subscriptionRequestId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscriptionRequestRPCTransferModel)) {
            return false;
        }
        final PushNotificationSubscriptionRequestRPCTransferModel that = (PushNotificationSubscriptionRequestRPCTransferModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getSubscriptionRequestId(), that.getSubscriptionRequestId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getSubscriptionRequestId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("subscriptionRequestId", getSubscriptionRequestId());
        return builder.build();
    }
}
