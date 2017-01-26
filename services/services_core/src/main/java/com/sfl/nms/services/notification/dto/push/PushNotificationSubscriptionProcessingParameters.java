package com.sfl.nms.services.notification.dto.push;

import com.sfl.nms.services.notification.model.push.PushNotificationProviderType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/20/15
 * Time: 11:19 AM
 */
public class PushNotificationSubscriptionProcessingParameters implements Serializable {

    private static final long serialVersionUID = 818765619114083978L;

    /* Properties */
    private Long userId;

    private Long userMobileDeviceId;

    private String deviceToken;

    private PushNotificationProviderType currentPushNotificationProviderType;

    private String currentProviderToken;

    private boolean subscribe;

    private String applicationType;

    /* Constructors */
    public PushNotificationSubscriptionProcessingParameters() {
    }

    public PushNotificationSubscriptionProcessingParameters(final Long userId, final Long userMobileDeviceId, final String deviceToken, final PushNotificationProviderType currentPushNotificationProviderType, final String currentProviderToken, final boolean subscribe, final String applicationType) {
        this.userId = userId;
        this.userMobileDeviceId = userMobileDeviceId;
        this.deviceToken = deviceToken;
        this.currentPushNotificationProviderType = currentPushNotificationProviderType;
        this.currentProviderToken = currentProviderToken;
        this.subscribe = subscribe;
        this.applicationType = applicationType;
    }

    /* Properties getters and setters */
    public Long getUserId() {
        return userId;
    }

    public PushNotificationSubscriptionProcessingParameters setUserId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getUserMobileDeviceId() {
        return userMobileDeviceId;
    }

    public PushNotificationSubscriptionProcessingParameters setUserMobileDeviceId(final Long userMobileDeviceId) {
        this.userMobileDeviceId = userMobileDeviceId;
        return this;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public PushNotificationSubscriptionProcessingParameters setDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public PushNotificationProviderType getCurrentPushNotificationProviderType() {
        return currentPushNotificationProviderType;
    }

    public PushNotificationSubscriptionProcessingParameters setCurrentPushNotificationProviderType(final PushNotificationProviderType currentPushNotificationProviderType) {
        this.currentPushNotificationProviderType = currentPushNotificationProviderType;
        return this;
    }

    public String getCurrentProviderToken() {
        return currentProviderToken;
    }

    public PushNotificationSubscriptionProcessingParameters setCurrentProviderToken(final String currentProviderToken) {
        this.currentProviderToken = currentProviderToken;
        return this;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public PushNotificationSubscriptionProcessingParameters setSubscribe(final boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscriptionProcessingParameters)) {
            return false;
        }
        final PushNotificationSubscriptionProcessingParameters that = (PushNotificationSubscriptionProcessingParameters) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getDeviceToken(), that.getDeviceToken());
        builder.append(this.getUserMobileDeviceId(), that.getUserMobileDeviceId());
        builder.append(this.getUserId(), that.getUserId());
        builder.append(this.getCurrentProviderToken(), that.getCurrentProviderToken());
        builder.append(this.getCurrentPushNotificationProviderType(), that.getCurrentPushNotificationProviderType());
        builder.append(this.isSubscribe(), that.isSubscribe());
        builder.append(this.getApplicationType(), that.getApplicationType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getDeviceToken());
        builder.append(this.getUserMobileDeviceId());
        builder.append(this.getUserId());
        builder.append(this.getCurrentProviderToken());
        builder.append(this.getCurrentPushNotificationProviderType());
        builder.append(this.isSubscribe());
        builder.append(this.getApplicationType());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("deviceToken", this.getDeviceToken());
        builder.append("userMobileDeviceId", this.getUserMobileDeviceId());
        builder.append("userId", this.getUserId());
        builder.append("currentProviderToken", this.getCurrentProviderToken());
        builder.append("currentPushNotificationProviderType", this.getCurrentPushNotificationProviderType());
        builder.append("subscribe", this.isSubscribe());
        builder.append("applicationType", this.getApplicationType());
        return builder.build();
    }
}
