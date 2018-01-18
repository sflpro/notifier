package com.sflpro.notifier.queue.amqp.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.queue.amqp.model.AbstractRPCTransferModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 4:28 PM
 */
public class NotificationRPCTransferModel extends AbstractRPCTransferModel {
    private static final long serialVersionUID = -5215994146311541084L;

    /* Properties */
    @JsonProperty(value = "notificationId", required = true)
    private Long notificationId;

    /* Constructors */
    public NotificationRPCTransferModel(final Long notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationRPCTransferModel() {
    }

    /* Properties getters and setters */
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(final Long notificationId) {
        this.notificationId = notificationId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationRPCTransferModel)) {
            return false;
        }
        final NotificationRPCTransferModel that = (NotificationRPCTransferModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getNotificationId(), that.getNotificationId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getNotificationId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("notificationId", getNotificationId());
        return builder.build();
    }
}
