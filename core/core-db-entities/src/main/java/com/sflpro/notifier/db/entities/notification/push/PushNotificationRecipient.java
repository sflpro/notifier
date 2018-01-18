package com.sflpro.notifier.db.entities.notification.push;

import com.sflpro.notifier.services.common.model.AbstractDomainUuIdAwareEntityModel;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
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
 * Time: 4:05 PM
 */
@Entity
@Table(name = "notification_push_recipient", indexes = {@Index(name = "IDX_push_notification_recipient_device_operating_system_type", columnList = "device_operating_system_type"), @Index(name = "IDX_push_notification_recipient_status", columnList = "status"), @Index(name = "IDX_push_notification_recipient_type", columnList = "type"), @Index(name = "IDX_push_notification_recipient_destination_route_token", columnList = "destination_route_token"), @Index(name = "IDX_push_notification_application_type", columnList = "application_type")}, uniqueConstraints = {
        @UniqueConstraint(name = "UK_push_notification_recipient_type_tkn_subs_id_app_type", columnNames = {"type", "destination_route_token", "subscription_id", "application_type"})})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class PushNotificationRecipient extends AbstractDomainUuIdAwareEntityModel {

    private static final long serialVersionUID = 4607098397294722795L;

    /* Properties */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, insertable = false, updatable = false)
    private PushNotificationProviderType type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private PushNotificationSubscription subscription;

    @Column(name = "destination_route_token", nullable = false)
    private String destinationRouteToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_operating_system_type", nullable = false)
    private DeviceOperatingSystemType deviceOperatingSystemType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PushNotificationRecipientStatus status;

    @Column(name = "application_type", nullable = false)
    private String applicationType;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_device_id", nullable = true)
    private UserDevice lastDevice;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY, orphanRemoval = false)
    private Set<PushNotificationRecipientDevice> devices;

    /* Constructors */
    public PushNotificationRecipient() {
        initializeDefaults();
    }

    public PushNotificationRecipient(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public PushNotificationProviderType getType() {
        return type;
    }

    public void setType(final PushNotificationProviderType type) {
        this.type = type;
    }

    public PushNotificationSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(final PushNotificationSubscription subscription) {
        this.subscription = subscription;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }

    public void setDestinationRouteToken(final String destinationRouteToken) {
        this.destinationRouteToken = destinationRouteToken;
    }

    public DeviceOperatingSystemType getDeviceOperatingSystemType() {
        return deviceOperatingSystemType;
    }

    public void setDeviceOperatingSystemType(final DeviceOperatingSystemType deviceOperatingSystemType) {
        this.deviceOperatingSystemType = deviceOperatingSystemType;
    }

    public PushNotificationRecipientStatus getStatus() {
        return status;
    }

    public void setStatus(final PushNotificationRecipientStatus status) {
        this.status = status;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public UserDevice getLastDevice() {
        return lastDevice;
    }

    public void setLastDevice(final UserDevice lastDevice) {
        this.lastDevice = lastDevice;
    }

    public Set<PushNotificationRecipientDevice> getDevices() {
        return devices;
    }

    public void setDevices(final Set<PushNotificationRecipientDevice> devices) {
        this.devices = devices;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        this.devices = new LinkedHashSet<>();
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationRecipient)) {
            return false;
        }
        final PushNotificationRecipient that = (PushNotificationRecipient) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getType(), that.getType());
        builder.append(this.getDestinationRouteToken(), that.getDestinationRouteToken());
        builder.append(this.getDeviceOperatingSystemType(), that.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType(), that.getApplicationType());
        builder.append(this.getStatus(), that.getStatus());
        builder.append(getIdOrNull(this.getSubscription()), getIdOrNull(that.getSubscription()));
        builder.append(getIdOrNull(this.getLastDevice()), getIdOrNull(that.getLastDevice()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getType());
        builder.append(this.getDestinationRouteToken());
        builder.append(this.getDeviceOperatingSystemType());
        builder.append(this.getApplicationType());
        builder.append(this.getStatus());
        builder.append(getIdOrNull(this.getSubscription()));
        builder.append(getIdOrNull(this.getLastDevice()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("type", this.getType());
        builder.append("destinationRouteToken", this.getDestinationRouteToken());
        builder.append("deviceOperatingSystemType", this.getDeviceOperatingSystemType());
        builder.append("applicationType", this.getApplicationType());
        builder.append("status", this.getStatus());
        builder.append("subscription", getIdOrNull(this.getSubscription()));
        builder.append("lastDevice", this.getLastDevice());
        return builder.build();
    }
}
