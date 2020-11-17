package com.sflpro.notifier.services.notification.dto.email;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationFileAttachment;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:29 PM
 */
public class EmailNotificationDto extends NotificationDto<EmailNotification> {

    private static final long serialVersionUID = -2505594013054500474L;

    /* Properties */
    private String recipientEmail;

    private String senderEmail;

    private Set<String> replyToEmails = new HashSet<>();

    private String userUuid;

    private Locale locale;

    private Set<EmailNotificationFileAttachment> fileAttachments;

    /* Constructors */
    public EmailNotificationDto(
            final String recipientEmail,
            final String senderEmail,
            final Set<String> replyToEmails,
            final String content,
            final String subject,
            final String clientIpAddress,
            final String templateName,
            final NotificationProviderType providerType
    ) {
        super(NotificationType.EMAIL, content, subject, clientIpAddress, templateName, providerType);
        this.recipientEmail = recipientEmail;
        this.senderEmail = senderEmail;
        this.replyToEmails = replyToEmails;
    }

    public EmailNotificationDto() {
        super(NotificationType.EMAIL);
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

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(final String userUuid) {
        this.userUuid = userUuid;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Set<EmailNotificationFileAttachment> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(final Set<EmailNotificationFileAttachment> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final EmailNotification notification) {
        super.updateDomainEntityProperties(notification);
        notification.setRecipientEmail(getRecipientEmail());
        notification.setSenderEmail(getSenderEmail());
        notification.setReplyToEmails(getReplyToEmails());
        notification.setTemplateName(getTemplateName());
        notification.setLocale(getLocale());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationDto)) {
            return false;
        }
        final EmailNotificationDto that = (EmailNotificationDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        builder.append(this.getSenderEmail(), that.getSenderEmail());
        builder.append(this.getReplyToEmails(), that.getReplyToEmails());
        builder.append(this.getTemplateName(), that.getTemplateName());
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
        builder.append(this.getTemplateName());
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
        builder.append("templateName", this.getTemplateName());
        builder.append("fileAttachments", this.getFileAttachments());
        return builder.build();
    }

}
