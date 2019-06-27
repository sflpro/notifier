package com.sflpro.notifier.services.notification.dto.email;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    private NotificationProviderType providerType;

    private String userUuid;

    /* Constructors */
    public EmailNotificationDto(final String recipientEmail,
                                final String senderEmail,
                                final NotificationProviderType providerType,
                                final String content,
                                final String subject,
                                final String clientIpAddress,
                                final String templateName) {
        super(NotificationType.EMAIL, content, subject, clientIpAddress,templateName);
        this.recipientEmail = recipientEmail;
        this.senderEmail = senderEmail;
        this.providerType = providerType;
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

    public NotificationProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(final NotificationProviderType providerType) {
        this.providerType = providerType;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(final String userUuid) {
        this.userUuid = userUuid;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final EmailNotification notification) {
        super.updateDomainEntityProperties(notification);
        notification.setRecipientEmail(getRecipientEmail());
        notification.setProviderType(getProviderType());
        notification.setSenderEmail(getSenderEmail());
        notification.setTemplateName(getTemplateName());
        notification.setHasSecureProperties(!getSecureProperties().isEmpty());
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
        builder.append(this.getProviderType(), that.getProviderType());
        builder.append(this.getTemplateName(), that.getTemplateName());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientEmail());
        builder.append(this.getSenderEmail());
        builder.append(this.getProviderType());
        builder.append(this.getTemplateName());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientEmail", this.getRecipientEmail());
        builder.append("senderEmail", this.getSenderEmail());
        builder.append("providerType", this.getProviderType());
        builder.append("templateName", this.getTemplateName());
        return builder.build();
    }
}
