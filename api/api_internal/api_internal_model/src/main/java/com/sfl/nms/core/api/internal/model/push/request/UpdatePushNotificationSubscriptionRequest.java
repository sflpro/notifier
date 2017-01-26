package com.sfl.nms.core.api.internal.model.push.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.nms.core.api.internal.model.common.AbstractRequestModel;
import com.sfl.nms.core.api.internal.model.common.result.ErrorResponseModel;
import com.sfl.nms.core.api.internal.model.common.result.ErrorType;
import com.sfl.nms.core.api.internal.model.device.DeviceOperatingSystemClientType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 10:42 AM
 */
public class UpdatePushNotificationSubscriptionRequest extends AbstractRequestModel {

    private static final long serialVersionUID = 1837809607717321384L;

    /* Properties */
    @JsonProperty("userDeviceToken")
    private String userDeviceToken;

    @JsonProperty("subscribe")
    private Boolean subscribe;

    @JsonProperty("lastUsedSubscriptionRequestUuId")
    private String lastUsedSubscriptionRequestUuId;

    @JsonProperty("deviceUuId")
    private String deviceUuId;

    @JsonProperty("deviceOperatingSystemType")
    private DeviceOperatingSystemClientType deviceOperatingSystemType;

    @JsonProperty("userUuId")
    private String userUuId;

    @JsonProperty("application")
    private String application;


    /* Constructors */
    public UpdatePushNotificationSubscriptionRequest() {
    }

    /* Properties getters and setters */
    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public void setUserDeviceToken(final String userDeviceToken) {
        this.userDeviceToken = userDeviceToken;
    }

    public Boolean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(final Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getLastUsedSubscriptionRequestUuId() {
        return lastUsedSubscriptionRequestUuId;
    }

    public void setLastUsedSubscriptionRequestUuId(final String lastUsedSubscriptionRequestUuId) {
        this.lastUsedSubscriptionRequestUuId = lastUsedSubscriptionRequestUuId;
    }

    public String getDeviceUuId() {
        return deviceUuId;
    }

    public void setDeviceUuId(final String deviceUuId) {
        this.deviceUuId = deviceUuId;
    }

    public DeviceOperatingSystemClientType getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public void setDeviceOperatingSystemType(final DeviceOperatingSystemClientType deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
    }

    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(final String userUuId) {
        this.userUuId = userUuId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(final String application) {
        this.application = application;
    }

    /* Validation methods */
    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = new ArrayList<>();
        if (StringUtils.isBlank(userDeviceToken)) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_DEVICE_TOKEN_MISSING));
        }
        if (subscribe == null) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_SUBSCRIBE_VALUE_MISSING));
        }
        if (StringUtils.isBlank(deviceUuId)) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_DEVICE_UUID_MISSING));
        }
        if (deviceOperatingSystemType == null) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_DEVICE_OPERATING_SYSTEM_TYPE_MISSING));
        }
        if (StringUtils.isBlank(userUuId)) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_USER_UUID_MISSING));
        }
        if (StringUtils.isBlank(application)) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_APPLICATION_MISSING));
        }
        return errors;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdatePushNotificationSubscriptionRequest)) {
            return false;
        }
        final UpdatePushNotificationSubscriptionRequest that = (UpdatePushNotificationSubscriptionRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getUserDeviceToken(), that.getUserDeviceToken());
        builder.append(this.getSubscribe(), that.getSubscribe());
        builder.append(this.getDeviceOperatingSystemType(), that.getDeviceOperatingSystemType());
        builder.append(this.getDeviceUuId(), that.getDeviceUuId());
        builder.append(this.getLastUsedSubscriptionRequestUuId(), that.getLastUsedSubscriptionRequestUuId());
        builder.append(this.getUserUuId(), that.getUserUuId());
        builder.append(this.getApplication(), that.getApplication());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUserDeviceToken());
        builder.append(this.getSubscribe());
        builder.append(this.getDeviceOperatingSystemType());
        builder.append(this.getDeviceUuId());
        builder.append(this.getLastUsedSubscriptionRequestUuId());
        builder.append(this.getUserUuId());
        builder.append(this.getApplication());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("userDeviceToken", this.getUserDeviceToken());
        builder.append("subscribe", this.getSubscribe());
        builder.append("deviceOperatingSystemType", this.getDeviceOperatingSystemType());
        builder.append("deviceUuId", this.getDeviceUuId());
        builder.append("lastUsedSubscriptionRequestUuId", this.getLastUsedSubscriptionRequestUuId());
        builder.append("userUuId", this.getUserUuId());
        builder.append("application", this.getApplication());
        return builder.build();
    }
}
