package com.sflpro.notifier.persistence.repositories.push;

import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientRepositoryCustom;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 2:33 PM
 */
public class PushNotificationRecipientRepositoryImpl implements PushNotificationRecipientRepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationRecipientRepositoryImpl.class);

    /* Constants */
    private static final String CRITERIA_AND = " and ";

    private static final String PARAMETER_NAME_DESTINATION_ROUTE_TOKEN = "destinationRouteToken";

    private static final String PARAMETER_NAME_STATUS = "status";

    private static final String PARAMETER_NAME_DEVICE_OPERATING_SYSTEM = "deviceOperatingSystemType";

    private static final String PARAMETER_NAME_PROVIDER_TYPE = "type";

    private static final String PARAMETER_NAME_SUBSCRIPTION_ID = "subscriptionId";

    private static final String PARAMETER_NAME_APPLICATION_TYPE = "applicationType";

    /* Dependencies */
    @PersistenceContext
    private EntityManager entityManager;

    /* Constructors */
    public PushNotificationRecipientRepositoryImpl() {
        LOGGER.debug("Initializing push notification recipient repository");
    }

    @Nonnull
    @Override
    public Long getPushNotificationRecipientsCount(@Nonnull final PushNotificationRecipientSearchParameters parameters) {
        assertSearchParametersNotNull(parameters);
        LOGGER.debug("Executing push notification recipients count lookup request for search parameters, parameters - {}", parameters);
        // Build query
        final TypedQuery<Long> typedQuery = buildFindRecipientsForSearchParametersTypedQuery(parameters, true, Long.class);
        // Execute query
        final Long customersCount = typedQuery.getSingleResult();
        LOGGER.debug("Successfully executed push notification recipients count lookup request for search parameters, parameters - {}", parameters);
        return customersCount;
    }

    @Nonnull
    @Override
    public List<PushNotificationRecipient> findPushNotificationRecipients(@Nonnull final PushNotificationRecipientSearchParameters parameters, final long startFrom, final int maxCount) {
        assertSearchParametersNotNull(parameters);
        Assert.isTrue(maxCount > 0, "Max count should be positive integer");
        Assert.isTrue(startFrom >= 0, "Start from should be positive integer or zero");
        LOGGER.debug("Executing push notification recipients search request for search parameters, parameters - {}, startFrom - {} ,maxCount - {}", parameters, startFrom, maxCount);
        // Build query
        final TypedQuery<PushNotificationRecipient> typedQuery = buildFindRecipientsForSearchParametersTypedQuery(parameters, false, PushNotificationRecipient.class);
        // Set pagination data
        typedQuery.setFirstResult((int) startFrom);
        typedQuery.setMaxResults(maxCount);
        // Execute query
        final List<PushNotificationRecipient> recipients = typedQuery.getResultList();
        LOGGER.debug("Successfully executed recipients lookup request for search parameters, parameters - {}, startFrom - {} ,maxCount - {}", parameters, startFrom, maxCount);
        return recipients;
    }

    /* Utility methods */
    private void assertSearchParametersNotNull(final PushNotificationRecipientSearchParameters parameters) {
        Assert.notNull(parameters, "Search parameters should not be null");
    }

    private <T> TypedQuery<T> buildFindRecipientsForSearchParametersTypedQuery(final PushNotificationRecipientSearchParameters parameters, final boolean countQuery, final Class<T> queryResultType) {
        final String queryString = buildFindRecipientsForSearchParametersQueryString(parameters, countQuery);
        // Create typed query
        final TypedQuery<T> typedQuery = entityManager.createQuery(queryString, queryResultType);
        if (parameters.getSubscriptionId() != null) {
            typedQuery.setParameter(PARAMETER_NAME_SUBSCRIPTION_ID, parameters.getSubscriptionId());
        }
        if (parameters.getProviderType() != null) {
            typedQuery.setParameter(PARAMETER_NAME_PROVIDER_TYPE, parameters.getProviderType());
        }
        if (parameters.getDeviceOperatingSystemType() != null) {
            typedQuery.setParameter(PARAMETER_NAME_DEVICE_OPERATING_SYSTEM, parameters.getDeviceOperatingSystemType());
        }
        if (parameters.getDestinationRouteToken() != null) {
            typedQuery.setParameter(PARAMETER_NAME_DESTINATION_ROUTE_TOKEN, parameters.getDestinationRouteToken());
        }
        if (parameters.getStatus() != null) {
            typedQuery.setParameter(PARAMETER_NAME_STATUS, parameters.getStatus());
        }
        if (parameters.getApplicationType() != null) {
            typedQuery.setParameter(PARAMETER_NAME_APPLICATION_TYPE, parameters.getApplicationType());
        }
        return typedQuery;
    }

    private String buildFindRecipientsForSearchParametersQueryString(final PushNotificationRecipientSearchParameters parameters, final boolean countQuery) {
        // Build query string
        final StringBuilder queryBuilder = new StringBuilder();
        if (countQuery) {
            queryBuilder.append(" select count(pnr) ");
        } else {
            queryBuilder.append(" select pnr ");
        }
        queryBuilder.append(" from PushNotificationRecipient pnr ");
        String criteriaPrefix = " where ";
        // Add destination route token criteria
        criteriaPrefix = appendRecipientDestinationRouteTokenCriteria(parameters, criteriaPrefix, queryBuilder);
        // Add status criteria
        criteriaPrefix = appendRecipientStatusCriteria(parameters, criteriaPrefix, queryBuilder);
        // Add operating system criteria
        criteriaPrefix = appendRecipientOperatingSystemCriteria(parameters, criteriaPrefix, queryBuilder);
        // Add provider type criteria
        criteriaPrefix = appendRecipientProviderTypeCriteria(parameters, criteriaPrefix, queryBuilder);
        // Add subscription criteria
        criteriaPrefix = appendRecipientSubscriptionCriteria(parameters, criteriaPrefix, queryBuilder);
        // Add application type criteria
        criteriaPrefix = appendRecipientApplicationTypeCriteria(parameters, criteriaPrefix, queryBuilder);
        // Append order by query
        if (!countQuery) {
            queryBuilder.append(" order by pnr.id asc ");
        }
        // Return query
        return queryBuilder.toString();
    }

    private String appendRecipientDestinationRouteTokenCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getDestinationRouteToken() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.destinationRouteToken=:" + PARAMETER_NAME_DESTINATION_ROUTE_TOKEN + " ");
        }
        return updatedCriteriaPrefix;
    }

    private String appendRecipientStatusCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getStatus() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.status=:" + PARAMETER_NAME_STATUS + " ");
        }
        return updatedCriteriaPrefix;
    }

    private String appendRecipientOperatingSystemCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getDeviceOperatingSystemType() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.deviceOperatingSystemType=:" + PARAMETER_NAME_DEVICE_OPERATING_SYSTEM + " ");
        }
        return updatedCriteriaPrefix;
    }

    private String appendRecipientProviderTypeCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getProviderType() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.type=:" + PARAMETER_NAME_PROVIDER_TYPE + " ");
        }
        return updatedCriteriaPrefix;
    }

    private String appendRecipientApplicationTypeCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getApplicationType() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.applicationType=:" + PARAMETER_NAME_APPLICATION_TYPE + " ");
        }
        return updatedCriteriaPrefix;
    }

    private String appendRecipientSubscriptionCriteria(final PushNotificationRecipientSearchParameters parameters, final String criteriaPrefix, final StringBuilder queryBuilder) {
        String updatedCriteriaPrefix = criteriaPrefix;
        if (parameters.getSubscriptionId() != null) {
            queryBuilder.append(criteriaPrefix);
            updatedCriteriaPrefix = CRITERIA_AND;
            queryBuilder.append(" pnr.subscription.id=:" + PARAMETER_NAME_SUBSCRIPTION_ID + " ");
        }
        return updatedCriteriaPrefix;
    }
}
