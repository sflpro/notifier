package com.sfl.nms.services.notification.dto.push;

import com.sfl.nms.services.common.dto.AbstractDomainEntityModelDto;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:11 AM
 */
public class PushNotificationSubscriptionRequestDto extends AbstractDomainEntityModelDto<PushNotificationSubscriptionRequest> {

    private static final long serialVersionUID = 2134645122916048974L;

    /* Properties */
    private boolean subscribe;

    private String userDeviceToken;

    private String previousSubscriptionRequestUuId;

    private String applicationType;

    /* Constructors */
    public PushNotificationSubscriptionRequestDto() {
    }

    public PushNotificationSubscriptionRequestDto(final String userDeviceToken, final String applicationType, final boolean subscribe, final String previousSubscriptionRequestUuId) {
        this.subscribe = subscribe;
        this.userDeviceToken = userDeviceToken;
        this.previousSubscriptionRequestUuId = previousSubscriptionRequestUuId;
        this.applicationType = applicationType;
    }

    /* Properties getters and setters */
    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(final boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public void setUserDeviceToken(final String userDeviceToken) {
        this.userDeviceToken = userDeviceToken;
    }

    public String getPreviousSubscriptionRequestUuId() {
        return previousSubscriptionRequestUuId;
    }

    public void setPreviousSubscriptionRequestUuId(final String previousSubscriptionRequestUuId) {
        this.previousSubscriptionRequestUuId = previousSubscriptionRequestUuId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final PushNotificationSubscriptionRequest request) {
        Assert.notNull(request, "Push notification subscription request should not be null");
        request.setUserDeviceToken(getUserDeviceToken());
        request.setPreviousSubscriptionRequestUuId(getPreviousSubscriptionRequestUuId());
        request.setSubscribe(isSubscribe());
        request.setApplicationType(getApplicationType());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscriptionRequestDto)) {
            return false;
        }
        final PushNotificationSubscriptionRequestDto that = (PushNotificationSubscriptionRequestDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getUserDeviceToken(), that.getUserDeviceToken());
        builder.append(this.getPreviousSubscriptionRequestUuId(), that.getPreviousSubscriptionRequestUuId());
        builder.append(this.getApplicationType(), that.getApplicationType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUserDeviceToken());
        builder.append(this.getPreviousSubscriptionRequestUuId());
        builder.append(this.getApplicationType());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("userDeviceToken", this.getUserDeviceToken());
        builder.append("previousSubscriptionRequestUuId", this.getPreviousSubscriptionRequestUuId());
        builder.append("applicationType", this.getApplicationType());
        return builder.build();
    }
}
