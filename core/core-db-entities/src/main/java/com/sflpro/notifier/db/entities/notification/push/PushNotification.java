package com.sflpro.notifier.db.entities.notification.push;


import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import com.sflpro.notifier.db.entities.NotificationProperty;
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
 * Date: 8/11/15
 * Time: 2:32 PM
 */
@Entity
@DiscriminatorValue(value = "PUSH")
@Table(name = "notification_push")
public class PushNotification extends Notification {

    private static final long serialVersionUID = -803404819866716200L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false, unique = false)
    private PushNotificationRecipient recipient;

    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<NotificationProperty> properties;

    /* Constructors */
    public PushNotification() {
        initializeDefaults();
    }

    public PushNotification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public PushNotificationRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipient subscription) {
        this.recipient = subscription;
    }

    public Set<NotificationProperty> getProperties() {
        return properties;
    }

    public void setProperties(final Set<NotificationProperty> properties) {
        this.properties = properties;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.PUSH);
        this.properties = new LinkedHashSet<>();
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotification)) {
            return false;
        }
        final PushNotification that = (PushNotification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(AbstractDomainEntityModel.getIdOrNull(this.getRecipient()), AbstractDomainEntityModel.getIdOrNull(that.getRecipient()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(AbstractDomainEntityModel.getIdOrNull(this.getRecipient()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("recipient", AbstractDomainEntityModel.getIdOrNull(this.getRecipient()));
        return builder.build();
    }
}
