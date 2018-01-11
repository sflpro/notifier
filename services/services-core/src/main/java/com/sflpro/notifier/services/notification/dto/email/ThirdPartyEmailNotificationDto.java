package com.sflpro.notifier.services.notification.dto.email;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.services.notification.model.NotificationType;
import com.sflpro.notifier.services.notification.model.email.ThirdPartyEmailNotification;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:34 PM
 */
public class ThirdPartyEmailNotificationDto extends NotificationDto<ThirdPartyEmailNotification> {
    private static final long serialVersionUID = -961544380418501076L;

    /* Properties */
    private String recipientEmail;

    private String senderEmail;

    private NotificationProviderType providerType;

    private String templateName;

    /* Constructors */
    public ThirdPartyEmailNotificationDto(final String recipientEmail, final String senderEmail, final NotificationProviderType providerType, final String templateName, final String content, final String subject, final String clientIpAddress) {
        super(NotificationType.EMAIL_THIRD_PARTY, content, subject, clientIpAddress);
        this.recipientEmail = recipientEmail;
        this.senderEmail = senderEmail;
        this.providerType = providerType;
        this.templateName = templateName;
    }

    public ThirdPartyEmailNotificationDto() {
        super(NotificationType.EMAIL_THIRD_PARTY);
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final ThirdPartyEmailNotification notification) {
        super.updateDomainEntityProperties(notification);
        notification.setRecipientEmail(getRecipientEmail());
        notification.setProviderType(getProviderType());
        notification.setTemplateName(getTemplateName());
        notification.setSenderEmail(getSenderEmail());
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
