package com.sflpro.notifier.api.model.sms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.notification.request.AbstractCreateNotificationRequest;
import com.sflpro.notifier.api.model.notification.request.AbstractTemplateAwareCreateNotificationRequest;
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
 * Time: 2:57 PM
 */
public class CreateSmsNotificationRequest extends AbstractTemplateAwareCreateNotificationRequest {

    /* Properties */
    @JsonProperty("recipientNumber")
    private String recipientNumber;

    /* Constructors */
    public CreateSmsNotificationRequest() {
        super();
    }

    /* Properties getters and setters */
    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(final String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    /* Validation methods */
    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = super.validateRequiredFields();
        if (StringUtils.isBlank(recipientNumber)) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_SMS_RECIPIENT_MISSING));
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
        final CreateSmsNotificationRequest that = (CreateSmsNotificationRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getRecipientNumber(), that.getRecipientNumber());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientNumber());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientNumber", this.getRecipientNumber());
        return builder.build();
    }
}
