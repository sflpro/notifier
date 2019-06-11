package com.sflpro.notifier.api.model.notification.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.AbstractRequestModel;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 8:00 PM
 */
public abstract class AbstractCreateNotificationRequest extends AbstractRequestModel {

    private static final long serialVersionUID = 8423183227450211673L;

    /* Properties */
    @JsonProperty("userUuId")
    private String userUuId;

    @JsonProperty("body")
    private String body;

    @JsonProperty("clientIpAddress")
    private String clientIpAddress;

    @JsonProperty("properties")
    private Map<String, String> properties;

    /* Constructors */
    public AbstractCreateNotificationRequest() {
        properties = new LinkedHashMap<>();
    }

    /* Properties getters and setters */
    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(final String userUuId) {
        this.userUuId = userUuId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(final String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    /* Validation methods */
    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = new ArrayList<>();
        if (StringUtils.isBlank(body)) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_BODY_MISSING));
        }
        return errors;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractCreateNotificationRequest)) {
            return false;
        }
        final AbstractCreateNotificationRequest that = (AbstractCreateNotificationRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getUserUuId(), that.getUserUuId());
        builder.append(this.getBody(), that.getBody());
        builder.append(this.getClientIpAddress(), that.getClientIpAddress());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUserUuId());
        builder.append(this.getBody());
        builder.append(this.getClientIpAddress());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("userUuId", this.getUserUuId());
        builder.append("body", this.getBody());
        builder.append("clientIpAddress", this.getClientIpAddress());
        builder.append("properties", this.getProperties());
        return builder.build();
    }
}
