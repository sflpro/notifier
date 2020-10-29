package com.sflpro.notifier.api.model.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:35 PM
 */
public class EmailNotificationModel extends NotificationModel {

    private static final long serialVersionUID = 1598311829038207601L;

    /* Properties */
    @JsonProperty("senderEmail")
    private String senderEmail;

    @JsonProperty("recipientEmail")
    private String recipientEmail;

    @JsonProperty("replyToEmails")
    private Set<String> replyToEmails;

    @JsonProperty("fileAttachments")
    private Set<EmailNotificationFileAttachmentModel> fileAttachments = Collections.emptySet();

    /* Constructors */
    public EmailNotificationModel() {
        //default constructor
    }

    /* Properties getters and setters */
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(final String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(final String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public Set<String> getReplyToEmails() {
        return replyToEmails;
    }

    public void setReplyToEmails(final Set<String> replyToEmails) {
        this.replyToEmails = replyToEmails;
    }

    public Set<EmailNotificationFileAttachmentModel> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(final Set<EmailNotificationFileAttachmentModel> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationModel)) {
            return false;
        }
        final EmailNotificationModel that = (EmailNotificationModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        builder.append(this.getSenderEmail(), that.getSenderEmail());
        builder.append(this.getReplyToEmails(), that.getReplyToEmails());
        builder.append(this.getFileAttachments(), that.getFileAttachments());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientEmail());
        builder.append(this.getSenderEmail());
        builder.append(this.getReplyToEmails());
        builder.append(this.getFileAttachments());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("replyToEmails", this.getReplyToEmails());
        builder.append("fileAttachments", this.getFileAttachments());
        return builder.build();
    }
}
