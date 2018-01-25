package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 10:29 AM
 */
public class PushNotificationRecipientNotFoundForLookupParametersException extends ServicesRuntimeException {

    private static final long serialVersionUID = -5415464359889659791L;

    /* Properties */
    private final Long subscriptionId;

    private final PushNotificationProviderType type;

    private final String destinationRouteToken;

    /* Constructors */
    public PushNotificationRecipientNotFoundForLookupParametersException(final Long subscriptionId, final PushNotificationProviderType type, final String destinationRouteToken) {
        super("No push notification recipient was found for subscription id - " + subscriptionId + ", type - " + type + ", destination route token - " + destinationRouteToken);
        this.subscriptionId = subscriptionId;
        this.type = type;
        this.destinationRouteToken = destinationRouteToken;
    }


    /* Properties getters and setters */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public PushNotificationProviderType getType() {
        return type;
    }

    public String getDestinationRouteToken() {
        return destinationRouteToken;
    }
}
