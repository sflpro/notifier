package com.sflpro.notifier.db.entities.notification;

import com.sflpro.notifier.db.entities.AbstractDomainUuIdAwareEntityModel;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
@Access(AccessType.FIELD)
@NamedEntityGraph(name = "Notification.ProcessingFlow", attributeNodes = @NamedAttributeNode("properties"))
public abstract class Notification extends AbstractDomainUuIdAwareEntityModel {
    private static final long serialVersionUID = -3642971556967427525L;

    /* Properties */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false, insertable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private NotificationState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", updatable = false, nullable = false)
    private NotificationProviderType providerType;

    @Column(name = "content", updatable = false, nullable = true, length = 20000)
    private String content;

    @Column(name = "subject", updatable = false, nullable = true, length = 500)
    private String subject;

    @Column(name = "client_ip_address", updatable = false, nullable = true)
    private String clientIpAddress;

    @Column(name = "provider_external_uuid", nullable = true)
    private String providerExternalUuId;

    @Column(name = "has_secure_properties", updatable = false, nullable = false)
    private boolean hasSecureProperties;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", referencedColumnName = "id", updatable = false, nullable = false)
    private List<NotificationProperty> properties;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Set<NotificationLabel> labels;

    /* Constructors */
    public Notification() {
        initializeDefaults();
    }

    public Notification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
        properties = new ArrayList<>();
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

    public boolean hasSecureProperties() {
        return hasSecureProperties;
    }

    public void setHasSecureProperties(final boolean hasSecureProperties) {
        this.hasSecureProperties = hasSecureProperties;
    }

    public List<NotificationProperty> getProperties() {
        return properties;
    }

    public void setProperties(final List<NotificationProperty> properties) {
        this.properties = properties;
    }

    public Set<NotificationLabel> getLabels() {
        return labels;
    }

    public void setLabels(Set<NotificationLabel> labels) {
        this.labels = labels;
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
        builder.append(this.hasSecureProperties(), that.hasSecureProperties());
        builder.append(this.getLabels(), that.getLabels());
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
        builder.append(this.hasSecureProperties());
        builder.append(this.getLabels());
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
        builder.append("has_secure_properties", this.hasSecureProperties());
        builder.append("labels", this.getLabels());
        return builder.build();
    }
}
