package com.sflpro.notifier.core.api.internal.model.push.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.common.AbstractResponseModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 10:55 AM
 */
public class UpdatePushNotificationSubscriptionResponse extends AbstractResponseModel {

    private static final long serialVersionUID = 382416047082806451L;

    /* Properties */
    @JsonProperty("subscriptionRequestUuId")
    private String subscriptionRequestUuId;

    /* Constructors */
    public UpdatePushNotificationSubscriptionResponse() {
        //default constructor
    }

    public UpdatePushNotificationSubscriptionResponse(final String subscriptionRequestUuId) {
        this.subscriptionRequestUuId = subscriptionRequestUuId;
    }

    /* Properties getters and setters */
    public String getSubscriptionRequestUuId() {
        return subscriptionRequestUuId;
    }

    public void setSubscriptionRequestUuId(final String subscriptionRequestUuId) {
        this.subscriptionRequestUuId = subscriptionRequestUuId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdatePushNotificationSubscriptionResponse)) {
            return false;
        }
        final UpdatePushNotificationSubscriptionResponse that = (UpdatePushNotificationSubscriptionResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getSubscriptionRequestUuId(), that.getSubscriptionRequestUuId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getSubscriptionRequestUuId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("subscriptionRequestUuId", this.getSubscriptionRequestUuId());
        return builder.build();
    }
}
