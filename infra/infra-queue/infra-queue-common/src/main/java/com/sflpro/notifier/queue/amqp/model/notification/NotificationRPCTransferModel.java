package com.sflpro.notifier.queue.amqp.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.queue.amqp.model.AbstractRPCTransferModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

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

    @JsonProperty(value = "secureProperties")
    private Map<String, String> secureProperties;

    /* Constructors */
    public NotificationRPCTransferModel(final Long notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationRPCTransferModel(final Long notificationId, final Map<String, String> secureProperties) {
        this.notificationId = notificationId;
        this.secureProperties = secureProperties;
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

    public Map<String, String> getSecureProperties() {
        return secureProperties;
    }

    public void setSecureProperties(final Map<String, String> secureProperties) {
        this.secureProperties = secureProperties;
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
        builder.append(getSecureProperties(), that.getSecureProperties());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getNotificationId());
        builder.append(getSecureProperties());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("notificationId", getNotificationId());
        builder.append("secureProperties", getSecureProperties().keySet());
        return builder.build();
    }
}
