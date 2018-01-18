package com.sflpro.notifier.services.notification.dto.push.sns;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:04 PM
 */
public class PushNotificationSnsRecipientDto extends PushNotificationRecipientDto<PushNotificationSnsRecipient> {

    private static final long serialVersionUID = -6132593093198332546L;

    /* Properties */
    private String platformApplicationArn;

    /* Constructors */
    public PushNotificationSnsRecipientDto() {
        super(PushNotificationProviderType.SNS);
    }

    public PushNotificationSnsRecipientDto(final String destinationRouteToken, final DeviceOperatingSystemType deviceOperatingSystemType, final String applicationType, final String platformApplicationArn) {
        super(PushNotificationProviderType.SNS, destinationRouteToken, deviceOperatingSystemType, applicationType);
        this.platformApplicationArn = platformApplicationArn;
    }

    /* Properties getters and setters */
    public String getPlatformApplicationArn() {
        return platformApplicationArn;
    }

    public void setPlatformApplicationArn(final String platformApplicationArn) {
        this.platformApplicationArn = platformApplicationArn;
    }

    /* Public interface methods */

    @Override
    public void updateDomainEntityProperties(final PushNotificationSnsRecipient recipient) {
        super.updateDomainEntityProperties(recipient);
        recipient.setPlatformApplicationArn(getPlatformApplicationArn());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSnsRecipientDto)) {
            return false;
        }
        final PushNotificationSnsRecipientDto that = (PushNotificationSnsRecipientDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPlatformApplicationArn(), that.getPlatformApplicationArn());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPlatformApplicationArn());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("platformApplicationArn", this.getPlatformApplicationArn());
        return builder.build();
    }
}
