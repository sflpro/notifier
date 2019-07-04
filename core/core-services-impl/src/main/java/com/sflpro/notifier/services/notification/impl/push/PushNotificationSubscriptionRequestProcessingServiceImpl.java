package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequestState;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestInvalidStateException;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 11:09 AM
 */
@Service
public class PushNotificationSubscriptionRequestProcessingServiceImpl implements PushNotificationSubscriptionRequestProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionRequestProcessingServiceImpl.class);

    /* Constants */
    private static final Set<PushNotificationSubscriptionRequestState> PROCESSING_ENABLED_STATES;

    static {
        final Set<PushNotificationSubscriptionRequestState> temporarySet = new HashSet<>();
        temporarySet.add(PushNotificationSubscriptionRequestState.CREATED);
        temporarySet.add(PushNotificationSubscriptionRequestState.FAILED);
        // Publish set
        PROCESSING_ENABLED_STATES = Collections.unmodifiableSet(temporarySet);
    }

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    @Autowired
    private PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    @Value("${push.notification.provider:SNS}")
    private PushNotificationProviderType defaultProviderType;

    /* Constructors */
    PushNotificationSubscriptionRequestProcessingServiceImpl() {
        LOGGER.debug("Initializing push notification subscription request processing service");
    }

    @Nonnull
    @Override
    public PushNotificationRecipient processPushNotificationSubscriptionRequest(@Nonnull final Long requestId) {
        Assert.notNull(requestId, "Push notification subscription request id should not be null");
        LOGGER.debug("Processing push notification subscription request with id - {}", requestId);
        final PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(requestId);
        // Assert push notification request state
        assertPushNotificationSubscriptionRequestState(request);
        // Update state
        updatePushNotificationSubscriptionRequestState(request.getId(), PushNotificationSubscriptionRequestState.PROCESSING);
        try {
            final PushNotificationSubscriptionProcessingParameters parameters = buildSubscriptionProcessingParameters(request);
            LOGGER.debug("Starting push notification subscription processing for request with id - {}, processing parameters - {}", request.getId(), parameters);
            final PushNotificationRecipient recipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
            // Update recipient on request
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(request.getId(), recipient.getId());
            LOGGER.debug("Successfully created push notification recipient for subscription request with id - {} and processing parameters - {}. Recipient - {}", requestId, parameters, recipient);
            updatePushNotificationSubscriptionRequestState(request.getId(), PushNotificationSubscriptionRequestState.PROCESSED);
            return recipient;
        } catch (final Exception ex) {
            updatePushNotificationSubscriptionRequestState(request.getId(), PushNotificationSubscriptionRequestState.FAILED);
            final String message = "Error occurred while processing push notification subscription request with id - " + request.getId();
            LOGGER.error(message, ex);
            throw new ServicesRuntimeException(message, ex);
        }
    }

    /* Utility methods */
    private void assertPushNotificationSubscriptionRequestState(final PushNotificationSubscriptionRequest request) {
        if (!PROCESSING_ENABLED_STATES.contains(request.getState())) {
            LOGGER.error("Push notification subscription request with id - {} has state - {} where as only requests with states- {} can be processed.", request.getId(), request.getState(), PROCESSING_ENABLED_STATES);
            throw new PushNotificationSubscriptionRequestInvalidStateException(request.getId(), request.getState(), PROCESSING_ENABLED_STATES);
        }
    }

    private void updatePushNotificationSubscriptionRequestState(final Long requestId, final PushNotificationSubscriptionRequestState state) {
        persistenceUtilityService.runInNewTransaction(() -> pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(requestId, state));
    }

    private PushNotificationSubscriptionProcessingParameters buildSubscriptionProcessingParameters(final PushNotificationSubscriptionRequest request) {
        // Extract last used token information
        final LastProviderTokenInformation lastProviderTokenInformation = extractLastUsedTokenInformationIfAvailable(request);
        // Build processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setDeviceToken(request.getUserDeviceToken());
        parameters.setSubscribe(request.isSubscribe());
        parameters.setCurrentPushNotificationProviderType(lastProviderTokenInformation.getLastProviderType());
        parameters.setCurrentProviderToken(lastProviderTokenInformation.getLastProviderToken());
        parameters.setUserMobileDeviceId(request.getUserMobileDevice().getId());
        parameters.setUserId(request.getUser().getId());
        parameters.setApplicationType(request.getApplicationType());
        // Return parameters
        return parameters;
    }

    private LastProviderTokenInformation extractLastUsedTokenInformationIfAvailable(final PushNotificationSubscriptionRequest request) {
        // Data to be used
        String lastProviderToken = null;
        PushNotificationProviderType lastProviderType = defaultProviderType;
        final String previouslyUsedSubscriptionRequestUuId = request.getPreviousSubscriptionRequestUuId();
        if (previouslyUsedSubscriptionRequestUuId != null) {
            // Check if subscription request exists
            final boolean previousRequestExists = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(previouslyUsedSubscriptionRequestUuId);
            if (previousRequestExists) {
                // Load previous request
                final PushNotificationSubscriptionRequest previousRequest = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(previouslyUsedSubscriptionRequestUuId);
                final PushNotificationRecipient recipient = previousRequest.getRecipient();
                if (recipient != null) {
                    lastProviderToken = recipient.getDestinationRouteToken();
                    lastProviderType = recipient.getType();
                }
            }
        }
        // Return result
        return new LastProviderTokenInformation(lastProviderToken, lastProviderType);
    }

    /* Properties getters and setters */
    public void setPushNotificationSubscriptionRequestService(final PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService) {
        this.pushNotificationSubscriptionRequestService = pushNotificationSubscriptionRequestService;
    }

    public void setPushNotificationSubscriptionProcessingService(final PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService) {
        this.pushNotificationSubscriptionProcessingService = pushNotificationSubscriptionProcessingService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    public void setDefaultProviderType(final PushNotificationProviderType defaultProviderType) {
        this.defaultProviderType = defaultProviderType;
    }

    /* Inner classes */
    private static class LastProviderTokenInformation {

        /* Properties */
        private final String lastProviderToken;

        private final PushNotificationProviderType lastProviderType;

        /* Constructors */
        LastProviderTokenInformation(final String lastProviderToken, final PushNotificationProviderType lastProviderType) {
            this.lastProviderToken = lastProviderToken;
            this.lastProviderType = lastProviderType;
        }

        /* Properties getters and setters */
        String getLastProviderToken() {
            return lastProviderToken;
        }

        PushNotificationProviderType getLastProviderType() {
            return lastProviderType;
        }
    }
}
