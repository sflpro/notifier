package com.sflpro.notifier.services.notification.dto.sms;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:32 PM
 */
public class SmsNotificationDto extends NotificationDto<SmsNotification> {
    private static final long serialVersionUID = -709211283003175401L;

    /* Properties */
    private String recipientMobileNumber;

    /* Constructors */
    public SmsNotificationDto(final String recipientMobileNumber, final String content, final String clientIpAddress,final NotificationProviderType providerType) {
        super(NotificationType.SMS, content, null, clientIpAddress,providerType);
        this.recipientMobileNumber = recipientMobileNumber;
    }

    public SmsNotificationDto() {
        super(NotificationType.SMS);
    }

    /* Properties getters and setters */
    public String getRecipientMobileNumber() {
        return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(final String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }
    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final SmsNotification notification) {
        super.updateDomainEntityProperties(notification);
        notification.setRecipientMobileNumber(getRecipientMobileNumber());
        notification.setTemplateName(getTemplateName());
        notification.setLocale(getLocale());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsNotificationDto)) {
            return false;
        }
        final SmsNotificationDto that = (SmsNotificationDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getRecipientMobileNumber(), that.getRecipientMobileNumber());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getRecipientMobileNumber());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipientMobileNumber", this.getRecipientMobileNumber());
        return builder.build();
    }
}
