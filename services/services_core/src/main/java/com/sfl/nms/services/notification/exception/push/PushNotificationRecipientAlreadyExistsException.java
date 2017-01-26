package com.sfl.nms.services.notification.exception.push;

import com.sfl.nms.services.common.exception.ServicesRuntimeException;
import com.sfl.nms.services.notification.model.push.PushNotificationProviderType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:45 PM
 */
public class PushNotificationRecipientAlreadyExistsException extends ServicesRuntimeException {

    private static final long serialVersionUID = -5548284425564518932L;

    /* Properties */
    private final PushNotificationProviderType type;

    private final String destinationRouteToken;

    private final Long subscriptionId;

    private final Long existingRecipientId;

    private final String applicationType;

    /* Constructors */
    public PushNotificationRecipientAlreadyExistsException(final Long existingRecipientId, final PushNotificationProviderType type, final String destinationRouteToken, final Long subscriptionId, final String applicationType) {
        super("Push notification recipient with id - " + existingRecipientId + " already exists for provider type - " + type + " and destination route token - " + destinationRouteToken + ", subscription id - " + subscriptionId + ", application type - " + applicationType);
        this.type = type;
        this.destinationRouteToken = destinationRouteToken;
        this.existingRecipientId = existingRecipientId;
        this.subscriptionId = subscriptionId;
        this.applicationType = applicationType;
    }

    /* Properties getters and setters */
    public PushNotificationProviderType getType() {
        return type;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }

    public Long getExistingRecipientId() {
        return existingRecipientId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public String getApplicationType() {
        return applicationType;
    }
}
