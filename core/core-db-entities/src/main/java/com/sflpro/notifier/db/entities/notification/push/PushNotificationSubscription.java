package com.sflpro.notifier.db.entities.notification.push;

import com.sflpro.notifier.services.common.model.AbstractDomainEntityModel;
import com.sflpro.notifier.services.user.model.User;
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
 * Time: 2:35 PM
 */
@Entity
@Table(name = "notification_push_subscription")
public class PushNotificationSubscription extends AbstractDomainEntityModel {

    private static final long serialVersionUID = -982169038244199845L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nms_user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private Set<PushNotificationRecipient> recipients;

    /* Constructors */
    public PushNotificationSubscription() {
        this.recipients = new LinkedHashSet<>();
    }

    /* Properties getters and setters */
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Set<PushNotificationRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(final Set<PushNotificationRecipient> recipients) {
        this.recipients = recipients;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscription)) {
            return false;
        }
        final PushNotificationSubscription that = (PushNotificationSubscription) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getIdOrNull(this.getUser()), getIdOrNull(that.getUser()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getIdOrNull(this.getUser()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("user", getIdOrNull(this.getUser()));
        return builder.build();
    }
}
