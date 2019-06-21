package com.sflpro.notifier.db.entities.notification.push;

import com.sflpro.notifier.db.entities.AbstractDomainUuIdAwareEntityModel;
import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 8:56 AM
 */
@Entity
@Table(name = "notification_push_subscription_request")
public class PushNotificationSubscriptionRequest extends AbstractDomainUuIdAwareEntityModel {

    private static final long serialVersionUID = -6889836382325962316L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nms_user_id", nullable = false, unique = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_device_id", nullable = false, unique = false)
    private UserDevice userMobileDevice;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PushNotificationSubscriptionRequestState state;

    @Column(name = "application_type", nullable = false)
    private String applicationType;

    @Column(name = "subscribe", nullable = false)
    private boolean subscribe;

    @Column(name = "user_device_token", nullable = false, length = 2000)
    private String userDeviceToken;

    @Column(name = "previous_subscription_request_uuid", nullable = true)
    private String previousSubscriptionRequestUuId;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = true, unique = false)
    private PushNotificationRecipient recipient;

    /* Constructors */
    public PushNotificationSubscriptionRequest() {
        initializeDefaults();
    }

    public PushNotificationSubscriptionRequest(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    /* Properties getters and setters */
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public UserDevice getUserMobileDevice() {
        return userMobileDevice;
    }

    public void setUserMobileDevice(final UserDevice userDevice) {
        this.userMobileDevice = userDevice;
    }

    public PushNotificationSubscriptionRequestState getState() {
        return state;
    }

    public void setState(final PushNotificationSubscriptionRequestState state) {
        this.state = state;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(final boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public void setUserDeviceToken(final String userDeviceToken) {
        this.userDeviceToken = userDeviceToken;
    }

    public String getPreviousSubscriptionRequestUuId() {
        return previousSubscriptionRequestUuId;
    }

    public void setPreviousSubscriptionRequestUuId(final String currentPushNotificationProviderToken) {
        this.previousSubscriptionRequestUuId = currentPushNotificationProviderToken;
    }

    public PushNotificationRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipient recipient) {
        this.recipient = recipient;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    /* Private utility methods */
    private void initializeDefaults() {
        this.state = PushNotificationSubscriptionRequestState.CREATED;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationSubscriptionRequest)) {
            return false;
        }
        final PushNotificationSubscriptionRequest that = (PushNotificationSubscriptionRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getUserDeviceToken(), that.getUserDeviceToken());
        builder.append(this.getState(), that.getState());
        builder.append(this.getApplicationType(), that.getApplicationType());
        builder.append(this.getPreviousSubscriptionRequestUuId(), that.getPreviousSubscriptionRequestUuId());
        builder.append(getIdOrNull(this.getUser()), getIdOrNull(that.getUser()));
        builder.append(getIdOrNull(this.getUserMobileDevice()), getIdOrNull(that.getUserMobileDevice()));
        builder.append(getIdOrNull(this.getRecipient()), getIdOrNull(that.getRecipient()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUserDeviceToken());
        builder.append(this.getState());
        builder.append(this.getApplicationType());
        builder.append(this.getPreviousSubscriptionRequestUuId());
        builder.append(getIdOrNull(this.getUser()));
        builder.append(getIdOrNull(this.getUserMobileDevice()));
        builder.append(getIdOrNull(this.getRecipient()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("userDeviceToken", this.getUserDeviceToken());
        builder.append("state", this.getState());
        builder.append("applicationType", this.getApplicationType());
        builder.append("previousSubscriptionRequestUuId", this.getPreviousSubscriptionRequestUuId());
        builder.append("user", getIdOrNull(this.getUser()));
        builder.append("userMobileDevice", getIdOrNull(this.getUserMobileDevice()));
        builder.append("recipient", getIdOrNull(this.getRecipient()));
        return builder.build();
    }
}
