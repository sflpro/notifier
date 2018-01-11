package com.sflpro.notifier.core.api.internal.model.email.template.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.common.AbstractRequestModel;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.template.EmailTemplateClientType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/16/16
 * Time 2:02 PM
 */
public abstract class AbstractTemplatedEmailRequest extends AbstractRequestModel {
    private static final long serialVersionUID = -679272115593303013L;

    /* Properties */
    private final EmailTemplateClientType emailTemplateType;

    @JsonProperty("recipientEmail")
    private String recipientEmail;

    @JsonProperty("senderEmail")
    private String senderEmail;

    @JsonProperty("userUuId")
    private String userUuId;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    /* Constructors */
    public AbstractTemplatedEmailRequest(final EmailTemplateClientType emailTemplateType) {
        this.emailTemplateType = emailTemplateType;
    }

    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        return new ArrayList<>();
    }

    /* Properties getters and setters */
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(final String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(final String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(final String userUuId) {
        this.userUuId = userUuId;
    }

    public EmailTemplateClientType getEmailTemplateType() {
        return emailTemplateType;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractTemplatedEmailRequest)) {
            return false;
        }
        final AbstractTemplatedEmailRequest that = (AbstractTemplatedEmailRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        builder.append(this.getSenderEmail(), that.getSenderEmail());
        builder.append(this.getUserUuId(), that.getUserUuId());
        builder.append(this.getEmailTemplateType(), that.getEmailTemplateType());
        builder.append(this.getPhoneNumber(), that.getPhoneNumber());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientEmail());
        builder.append(this.getSenderEmail());
        builder.append(this.getUserUuId());
        builder.append(this.getEmailTemplateType());
        builder.append(this.getPhoneNumber());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("userUuId", this.getUserUuId());
        builder.append("emailTemplateType", this.getEmailTemplateType());
        builder.append("phoneNumber", this.getPhoneNumber());
        return builder.build();
    }
}
