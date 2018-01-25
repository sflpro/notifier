package com.sflpro.notifier.db.entities.notification.push;


import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import com.sflpro.notifier.db.entities.device.UserDevice;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/11/15
 * Time: 4:21 PM
 */
@Entity
@Table(name = "notification_push_recipient_device", uniqueConstraints = {
        @UniqueConstraint(name = "UK_push_notification_recipient_device_device_recipient", columnNames = {"device_id", "recipient_id"})})
public class PushNotificationRecipientDevice extends AbstractDomainEntityModel {

    private static final long serialVersionUID = -238707379084715694L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false, unique = false)
    private UserDevice device;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false, unique = false)
    private PushNotificationRecipient recipient;

    /* Constructors */
    public PushNotificationRecipientDevice() {
        super();
    }

    /* Properties getters and setters */
    public UserDevice getDevice() {
        return device;
    }

    public void setDevice(final UserDevice device) {
        this.device = device;
    }

    public PushNotificationRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipient recipient) {
        this.recipient = recipient;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationRecipientDevice)) {
            return false;
        }
        final PushNotificationRecipientDevice that = (PushNotificationRecipientDevice) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getIdOrNull(this.getDevice()), getIdOrNull(that.getDevice()));
        builder.append(getIdOrNull(this.getRecipient()), getIdOrNull(that.getRecipient()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getIdOrNull(this.getDevice()));
        builder.append(getIdOrNull(this.getRecipient()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("device", getIdOrNull(this.getDevice()));
        builder.append("recipient", getIdOrNull(this.getRecipient()));
        return builder.build();
    }
}
