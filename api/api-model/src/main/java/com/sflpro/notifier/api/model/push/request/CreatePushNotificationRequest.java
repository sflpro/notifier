package com.sflpro.notifier.api.model.push.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.notification.request.AbstractCreateNotificationRequest;
import com.sflpro.notifier.api.model.push.PushNotificationPropertyModel;
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
 * Date: 1/13/16
 * Time: 6:22 PM
 */
public class CreatePushNotificationRequest extends AbstractCreateNotificationRequest {
    private static final long serialVersionUID = 155923951040020310L;

    /* Properties */
    @JsonProperty("properties")
    private List<PushNotificationPropertyModel> properties;

    @JsonProperty("subject")
    private String subject;

    /* Constructors */
    public CreatePushNotificationRequest() {
        this.properties = new ArrayList<>();
    }

    /* Properties getters and setters */
    public List<PushNotificationPropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(final List<PushNotificationPropertyModel> properties) {
        this.properties = properties;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /* Validation methods */
    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = super.validateRequiredFields();
        if (StringUtils.isBlank(getUserUuId())) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_USER_MISSING));
        }
        return errors;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatePushNotificationRequest)) {
            return false;
        }
        final CreatePushNotificationRequest that = (CreatePushNotificationRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getProperties(), that.getProperties());
        builder.append(this.getSubject(), that.getSubject());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getProperties());
        builder.append(this.getSubject());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("properties", this.getProperties());
        builder.append("subject", this.getSubject());
        return builder.build();
    }
}
