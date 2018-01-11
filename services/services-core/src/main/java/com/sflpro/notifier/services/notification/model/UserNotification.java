package com.sflpro.notifier.services.notification.model;

import com.sflpro.notifier.services.common.model.AbstractDomainEntityModel;
import com.sflpro.notifier.services.user.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 12:56 PM
 */
@Entity
@Table(name = "notification_user")
public class UserNotification extends AbstractDomainEntityModel {
    private static final long serialVersionUID = -67239256787257865L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nms_user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false, unique = true)
    private Notification notification;

    /* Constructors */
    public UserNotification() {
    }

    /* Properties getters and setters */
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(final Notification notification) {
        this.notification = notification;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserNotification)) {
            return false;
        }
        final UserNotification that = (UserNotification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getIdOrNull(this.getUser()), getIdOrNull(that.getUser()));
        builder.append(getIdOrNull(this.getNotification()), getIdOrNull(that.getNotification()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getIdOrNull(this.getUser()));
        builder.append(getIdOrNull(this.getNotification()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("user", getIdOrNull(this.getUser()));
        builder.append("notification", getIdOrNull(this.getNotification()));
        return builder.build();
    }
}
