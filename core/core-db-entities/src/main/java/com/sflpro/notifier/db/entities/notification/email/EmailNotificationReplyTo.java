package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_email_reply_to")
public class EmailNotificationReplyTo extends AbstractDomainEntityModel {
    private static final long serialVersionUID = -3654936373396071600L;

    /* Properties */
    @Column(name = "email", nullable = false)
    private String email;

    public EmailNotificationReplyTo() {
        super();
    }

    public EmailNotificationReplyTo(final String email) {
        this();
        this.email = email;
    }

    /* Properties getters and setters */

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationReplyTo)) {
            return false;
        }
        final EmailNotificationReplyTo that = (EmailNotificationReplyTo) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getEmail(), that.getEmail());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getEmail());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("email", this.getEmail());
        return builder.build();
    }
}
