package com.sflpro.notifier.api.model.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.notification.request.AbstractTemplatedCreateNotificationRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:36 PM
 */
public class CreateEmailNotificationRequest extends AbstractTemplatedCreateNotificationRequest {

    private static final long serialVersionUID = 8423183227450211673L;

    /* Properties */
    @JsonProperty("recipientEmail")
    private String recipientEmail;

    @JsonProperty("senderEmail")
    private String senderEmail;

    @JsonProperty("replyToEmails")
    private Set<String> replyToEmails = new HashSet<>();

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("fileAttachments")
    private Set<EmailNotificationFileAttachmentRequest> fileAttachments = Collections.emptySet();

    /* Constructors */
    public CreateEmailNotificationRequest() {
        super();
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

    public Set<String> getReplyToEmails() {
        return replyToEmails;
    }

    public void setReplyToEmails(final Set<String> replyToEmails) {
        this.replyToEmails = replyToEmails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public Set<EmailNotificationFileAttachmentRequest> getFileAttachments() {
        return fileAttachments;
    }

    public CreateEmailNotificationRequest setFileAttachments(final Set<EmailNotificationFileAttachmentRequest> fileAttachments) {
        this.fileAttachments = fileAttachments;
        return this;
    }

    /* Validation methods */
    @Nonnull
    @Override
    public List<ErrorResponseModel> validateRequiredFields() {
        final List<ErrorResponseModel> errors = super.validateRequiredFields();
        if (StringUtils.isBlank(recipientEmail)) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_EMAIL_RECIPIENT_ADDRESS_MISSING));
        }
        if (StringUtils.isBlank(senderEmail)) {
            errors.add(new ErrorResponseModel(ErrorType.NOTIFICATION_EMAIL_SENDER_ADDRESS_MISSING));
        }
        return errors;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateEmailNotificationRequest)) {
            return false;
        }
        final CreateEmailNotificationRequest that = (CreateEmailNotificationRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getSenderEmail(), that.getSenderEmail());
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        builder.append(this.getReplyToEmails(), that.getReplyToEmails());
        builder.append(this.getSubject(), that.getSubject());
        builder.append(this.getTemplateName(), that.getTemplateName());
        builder.append(this.getProperties(), that.getProperties());
        builder.append(this.getSecureProperties(), that.getSecureProperties());
        builder.append(this.getLocale(), that.getLocale());
        builder.append(this.fileAttachments, that.getFileAttachments());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getSenderEmail());
        builder.append(this.getRecipientEmail());
        builder.append(this.getReplyToEmails());
        builder.append(this.getSubject());
        builder.append(this.getTemplateName());
        builder.append(this.getProperties());
        builder.append(this.getSecureProperties());
        builder.append(this.getLocale());
        builder.append(this.getFileAttachments());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("replyToEmails", this.getReplyToEmails());
        builder.append("subject", this.getSubject());
        builder.append("templateName", this.getTemplateName());
        builder.append("properties", this.getProperties());
        if (this.getSecureProperties() != null) {
            builder.append("secureProperties", this.getSecureProperties().keySet());
        }
        builder.append("language", this.getLocale());
        builder.append("fileAttachments", this.getFileAttachments());
        return builder.build();
    }
}
