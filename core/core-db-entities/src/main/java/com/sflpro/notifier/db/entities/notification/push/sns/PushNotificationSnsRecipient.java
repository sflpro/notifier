package com.sflpro.notifier.db.entities.notification.push.sns;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/11/15
 * Time: 4:32 PM
 */
@Entity
@DiscriminatorValue(value = "SNS")
@Table(name = "notification_push_recipient_sns")
public class PushNotificationSnsRecipient extends PushNotificationRecipient {

    private static final long serialVersionUID = -5327382264360548221L;

    /* Properties */
    @Column(name = "platform_application_arn", nullable = false, length = 500)
    private String platformApplicationArn;

    /* Constructors */
    public PushNotificationSnsRecipient() {
        initializeDefaults();
    }

    public PushNotificationSnsRecipient(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public String getPlatformApplicationArn() {
        return platformApplicationArn;
    }

    public void setPlatformApplicationArn(final String platformApplicationArn) {
        this.platformApplicationArn = platformApplicationArn;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        setType(PushNotificationProviderType.SNS);
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSnsRecipient)) {
            return false;
        }
        final PushNotificationSnsRecipient that = (PushNotificationSnsRecipient) o;
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
