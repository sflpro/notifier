package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Locale;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 12:38 PM
 */
@Entity
@DiscriminatorValue(value = "EMAIL")
@Table(name = "notification_email")
public class EmailNotification extends Notification {
    private static final long serialVersionUID = 2667943106211606010L;

    /* Properties */
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "sender_email", nullable = true)
    private String senderEmail;

    @Column(name = "template_name", nullable = true)
    private String templateName;

    @Column(name = "locale")
    private Locale locale;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_email_id", referencedColumnName = "id")
    private Set<EmailNotificationFileAttachment> fileAttachments;

    /* Constructors */
    public EmailNotification() {
        initializeDefaults();
    }

    public EmailNotification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.EMAIL);
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotification)) {
            return false;
        }
        final EmailNotification that = (EmailNotification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        builder.append(this.getSenderEmail(), that.getSenderEmail());
        builder.append(this.getTemplateName(), that.getTemplateName());
        builder.append(this.getLocale(), that.getLocale());
        builder.append(this.getFileAttachments(), that.getFileAttachments());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientEmail());
        builder.append(this.getSenderEmail());
        builder.append(this.getTemplateName());
        builder.append(this.getLocale());
        builder.append(this.getFileAttachments());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("templateName", this.getTemplateName());
        builder.append("locale", this.getLocale());
        builder.append("fileAttachments", this.getFileAttachments());
        return builder.build();
    }

}
