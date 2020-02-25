package com.sflpro.notifier.api.model.push.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.AbstractRequestModel;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.push.PushNotificationPropertyModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vazgen Danielyan
 * Date: 2/24/20
 * Time: 4:28 PM
 */
public class CreateTemplatedPushNotificationRequest extends AbstractRequestModel {

    //region Properties

    @JsonProperty("userUuId")
    private String userUuId;

    @JsonProperty("template")
    private String template;

    @JsonProperty("clientIpAddress")
    private String clientIpAddress;

    @JsonProperty("locale")
    private Locale locale;

    @JsonProperty("properties")
    private List<PushNotificationPropertyModel> properties;

    //endregion

    //region Getters and Setters

    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(final String userUuId) {
        this.userUuId = userUuId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(final String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public List<PushNotificationPropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(final List<PushNotificationPropertyModel> properties) {
        this.properties = properties;
    }

    //endregion

    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = new ArrayList<>();
        if (StringUtils.isBlank(getUserUuId())) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_USER_MISSING));
        }
        if (StringUtils.isBlank(getTemplate())) {
            errors.add(new ErrorResponseModel(ErrorType.SUBSCRIPTION_PUSH_TEMPLATE_MISSING));
        }
        return errors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CreateTemplatedPushNotificationRequest rhs = (CreateTemplatedPushNotificationRequest) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.userUuId, rhs.userUuId)
                .append(this.template, rhs.template)
                .append(this.clientIpAddress, rhs.clientIpAddress)
                .append(this.locale, rhs.locale)
                .append(this.properties, rhs.properties)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(userUuId)
                .append(template)
                .append(clientIpAddress)
                .append(locale)
                .append(properties)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("userUuId", userUuId)
                .append("template", template)
                .append("clientIpAddress", clientIpAddress)
                .append("locale", locale)
                .append("properties", properties)
                .toString();
    }
}
