package com.sflpro.notifier.api.model.sms.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.notification.request.AbstractCreateNotificationRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 2:57 PM
 */
public class CreateSmsNotificationRequest extends AbstractCreateNotificationRequest {

    /* Properties */
    @JsonProperty("recipientNumber")
    private String recipientNumber;

    @JsonProperty("templateName")
    private String templateName;

    @JsonProperty("properties")
    private Map<String, String> properties;

    @JsonProperty("secureProperties")
    private Map<String, String> secureProperties;

    /* Constructors */
    public CreateSmsNotificationRequest() {
        super();
        properties = new LinkedHashMap<>();
        secureProperties = new LinkedHashMap<>();
    }

    /* Properties getters and setters */
    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(final String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getSecureProperties() {
        return secureProperties;
    }

    public void setSecureProperties(final Map<String, String> secureProperties) {
        this.secureProperties = secureProperties;
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
        builder.append(this.getTemplateName(), that.getTemplateName());
        builder.append(this.getProperties(), that.getProperties());
        builder.append(this.getSecureProperties(), that.getSecureProperties());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientNumber());
        builder.append(this.getTemplateName());
        builder.append(this.getProperties());
        builder.append(this.getSecureProperties());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientNumber", this.getRecipientNumber());
        builder.append("templateName", this.getTemplateName());
        builder.append("properties", this.getProperties());
        builder.append("secureProperties", this.getSecureProperties());
        return builder.build();
    }
}
