package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.LinkedHashSet;
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

    @OneToMany(mappedBy = "emailNotification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EmailNotificationProperty> properties;

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

    public Set<EmailNotificationProperty> getProperties() {
        return properties;
    }

    public void setProperties(final Set<EmailNotificationProperty> properties) {
        this.properties = properties;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.EMAIL);
        this.properties = new LinkedHashSet<>();
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
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientEmail());
        builder.append(this.getSenderEmail());
        builder.append(this.getTemplateName());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("templateName", this.getTemplateName());
        return builder.build();
    }

}
