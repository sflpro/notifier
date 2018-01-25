package com.sflpro.notifier.db.repositories.repositories.notification.push.impl;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientRepositoryCustom;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientSearchFilter;
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
    public Long getPushNotificationRecipientsCount(@Nonnull final PushNotificationRecipientSearchFilter parameters) {
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
    public List<PushNotificationRecipient> findPushNotificationRecipients(@Nonnull final PushNotificationRecipientSearchFilter parameters, final long startFrom, final int maxCount) {
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
    private void assertSearchParametersNotNull(final PushNotificationRecipientSearchFilter parameters) {
        Assert.notNull(parameters, "Search parameters should not be null");
    }

    private <T> TypedQuery<T> buildFindRecipientsForSearchParametersTypedQuery(final PushNotificationRecipientSearchFilter parameters, final boolean countQuery, final Class<T> queryResultType) {
        final String queryString = buildFindRecipientsForSearchParametersQueryString(countQuery);
        // Create typed query
        final TypedQuery<T> typedQuery = entityManager.createQuery(queryString, queryResultType);
        typedQuery.setParameter(PARAMETER_NAME_SUBSCRIPTION_ID, parameters.getSubscriptionId());
        typedQuery.setParameter(PARAMETER_NAME_PROVIDER_TYPE, parameters.getProviderType());
        typedQuery.setParameter(PARAMETER_NAME_DEVICE_OPERATING_SYSTEM, parameters.getDeviceOperatingSystemType());
        typedQuery.setParameter(PARAMETER_NAME_DESTINATION_ROUTE_TOKEN, parameters.getDestinationRouteToken());
        typedQuery.setParameter(PARAMETER_NAME_STATUS, parameters.getStatus());
        typedQuery.setParameter(PARAMETER_NAME_APPLICATION_TYPE, parameters.getApplicationType());
        return typedQuery;
    }

    private String buildFindRecipientsForSearchParametersQueryString(final boolean countQuery) {
        // Build query string
        final StringBuilder queryBuilder = new StringBuilder();
        if (countQuery) {
            queryBuilder.append(" select count(pnr) ");
        } else {
            queryBuilder.append(" select pnr ");
        }
        queryBuilder.append(" from PushNotificationRecipient pnr ");
        queryBuilder.append(" where ");
        queryBuilder.append("( :" + PARAMETER_NAME_DESTINATION_ROUTE_TOKEN + " is null or pnr.destinationRouteToken=:" + PARAMETER_NAME_DESTINATION_ROUTE_TOKEN + ") ");
        queryBuilder.append(" and ( :" + PARAMETER_NAME_STATUS + " is null or pnr.status=:" + PARAMETER_NAME_STATUS + ") ");
        queryBuilder.append(" and ( :" + PARAMETER_NAME_DEVICE_OPERATING_SYSTEM + " is null or pnr.deviceOperatingSystemType=:" + PARAMETER_NAME_DEVICE_OPERATING_SYSTEM + ") ");
        queryBuilder.append(" and ( :" + PARAMETER_NAME_PROVIDER_TYPE + " is null or pnr.type=:" + PARAMETER_NAME_PROVIDER_TYPE + ") ");
        queryBuilder.append(" and ( :" + PARAMETER_NAME_APPLICATION_TYPE + " is null or pnr.applicationType=:" + PARAMETER_NAME_APPLICATION_TYPE + ") ");
        queryBuilder.append(" and ( :" + PARAMETER_NAME_SUBSCRIPTION_ID + " is null or pnr.subscription.id=:" + PARAMETER_NAME_SUBSCRIPTION_ID + ") ");
        // Append order by query
        if (!countQuery) {
            queryBuilder.append(" order by pnr.id asc ");
        }
        // Return query
        return queryBuilder.toString();
    }
}
