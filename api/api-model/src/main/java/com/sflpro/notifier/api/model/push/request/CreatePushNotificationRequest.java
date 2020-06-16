package com.sflpro.notifier.api.model.push.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.notification.request.AbstractTemplatedCreateNotificationRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:22 PM
 */
public class CreatePushNotificationRequest extends AbstractTemplatedCreateNotificationRequest {

    private static final long serialVersionUID = 155923951040020310L;

    @JsonProperty("subject")
    private String subject;

    public CreatePushNotificationRequest() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = super.validateRequiredFields();
        if (StringUtils.isBlank(getUserUuId())) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_USER_MISSING));
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
        CreatePushNotificationRequest rhs = (CreatePushNotificationRequest) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.subject, rhs.subject)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(subject)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("subject", subject)
                .toString();
    }
}
