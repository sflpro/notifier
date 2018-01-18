package com.sflpro.notifier.db.entities.notification;

import com.sflpro.notifier.services.common.model.AbstractDomainUuIdAwareEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 12:27 PM
 */

@Entity
@Table(name = "notification")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Notification extends AbstractDomainUuIdAwareEntityModel {
    private static final long serialVersionUID = -3642971556967427525L;

    /* Properties */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, insertable = false, updatable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private NotificationState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false)
    private NotificationProviderType providerType;

    @Column(name = "content", nullable = true, length = 20000)
    private String content;

    @Column(name = "subject", nullable = true, length = 500)
    private String subject;

    @Column(name = "client_ip_address", nullable = true)
    private String clientIpAddress;

    @Column(name = "provider_external_uuid", nullable = true)
    private String providerExternalUuId;

    /* Constructors */
    public Notification() {
        initializeDefaults();
    }

    public Notification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public NotificationType getType() {
        return type;
    }

    public void setType(final NotificationType type) {
        this.type = type;
    }

    public NotificationState getState() {
        return state;
    }

    public void setState(final NotificationState state) {
        this.state = state;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(final String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public NotificationProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(final NotificationProviderType providerType) {
        this.providerType = providerType;
    }

    public String getProviderExternalUuId() {
        return providerExternalUuId;
    }

    public void setProviderExternalUuId(final String providerExternalUuId) {
        this.providerExternalUuId = providerExternalUuId;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        this.state = NotificationState.CREATED;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        final Notification that = (Notification) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getType(), that.getType());
        builder.append(this.getState(), that.getState());
        builder.append(this.getProviderType(), that.getProviderType());
        builder.append(this.getClientIpAddress(), that.getClientIpAddress());
        builder.append(this.getContent(), that.getContent());
        builder.append(this.getSubject(), that.getSubject());
        builder.append(this.getProviderExternalUuId(), that.getProviderExternalUuId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getType());
        builder.append(this.getState());
        builder.append(this.getProviderType());
        builder.append(this.getClientIpAddress());
        builder.append(this.getContent());
        builder.append(this.getSubject());
        builder.append(this.getProviderExternalUuId());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("type", this.getType());
        builder.append("state", this.getState());
        builder.append("providerType", this.getProviderType());
        builder.append("clientIpAddress", this.getClientIpAddress());
        builder.append("content", this.getContent());
        builder.append("subject", this.getSubject());
        builder.append("providerExternalUuId", this.getProviderExternalUuId());
        return builder.build();
    }
}
