package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.repositories.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientSearchFilter;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:14 PM
 */
@Service
public class PushNotificationRecipientServiceImpl extends AbstractPushNotificationRecipientServiceImpl<PushNotificationRecipient> implements PushNotificationRecipientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationRecipientServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationRecipientRepository pushNotificationRecipientRepository;

    /* Constructors */
    public PushNotificationRecipientServiceImpl() {
        LOGGER.debug("Initializing push notification recipient service");
    }

    @Nonnull
    @Override
    public List<PushNotificationRecipient> getPushNotificationRecipientsForSearchParameters(@Nonnull final PushNotificationRecipientSearchParameters searchParameters, @Nonnull final Long startFrom, @Nonnull final Integer maxCount) {
        assertSearchParameters(searchParameters);
        Assert.notNull(startFrom, "Start from should not be null");
        Assert.isTrue(startFrom >= 0L, "Start from should be greater or equal then 0");
        Assert.notNull(maxCount, "Results max count should not be null");
        Assert.isTrue(maxCount > 0, "Max count should be greater then 0");
        LOGGER.debug("Searching for push notification recipients for search parameters - {}, start from - {}, max count - {}", searchParameters, startFrom, maxCount);
        final List<PushNotificationRecipient> recipients = pushNotificationRecipientRepository.findPushNotificationRecipients(convertParametersToFilter(searchParameters), startFrom, maxCount);
        LOGGER.debug("Got {} recipients for search parameters - {}, start from - {}, max count - {}", recipients.size(), searchParameters, startFrom, maxCount);
        return recipients;
    }

    @Nonnull
    @Override
    public Long getPushNotificationRecipientsCountForSearchParameters(@Nonnull final PushNotificationRecipientSearchParameters searchParameters) {
        assertSearchParameters(searchParameters);
        LOGGER.debug("Getting push notification recipients count for payment search parameters - {}", searchParameters);
        final Long count = pushNotificationRecipientRepository.getPushNotificationRecipientsCount(convertParametersToFilter(searchParameters));
        LOGGER.debug("Found - {} push notification recipients for search parameters - {}", count, searchParameters);
        return count;
    }

    /* Utility methods */
    @Override
    protected AbstractPushNotificationRecipientRepository<PushNotificationRecipient> getRepository() {
        return pushNotificationRecipientRepository;
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final PushNotificationRecipientDto recipientDto) {
        assertPushNotificationSubscriptionIdNotNull(subscriptionId);
        assertPushNotificationRecipientDto(recipientDto);
        LOGGER.debug("Creating push notification recipient DTO for subscription with id - {}, DTO - {}", subscriptionId, recipientDto);
        final PushNotificationSubscription subscription = getPushNotificationSubscriptionService().getPushNotificationSubscriptionById(subscriptionId);
        assertNoRecipientExists(recipientDto.getType(), subscription, recipientDto.getDestinationRouteToken(), recipientDto.getApplicationType());
        // Create new push notification recipient
        PushNotificationRecipient recipient = new PushNotificationRecipient(recipientDto.getType(),true);
        recipientDto.updateDomainEntityProperties(recipient);
        recipient.setStatus(PushNotificationRecipientStatus.ENABLED);
        recipient.setSubscription(subscription);
        // Persist recipient
        recipient = pushNotificationRecipientRepository.save(recipient);
        LOGGER.debug("Successfully created push notification recipient for id - {}, recipient - {}", recipient.getId(), recipient);
        return recipient;
    }

    @Override
    protected Class<PushNotificationRecipient> getInstanceClass() {
        return PushNotificationRecipient.class;
    }

    private void assertSearchParameters(final PushNotificationRecipientSearchParameters searchParameters) {
        Assert.notNull(searchParameters, "Search parameters should not be null");
    }

    private PushNotificationRecipientSearchFilter convertParametersToFilter(PushNotificationRecipientSearchParameters parameters) {
        PushNotificationRecipientSearchFilter filter = new PushNotificationRecipientSearchFilter();
        filter.setApplicationType(parameters.getApplicationType());
        filter.setDestinationRouteToken(parameters.getDestinationRouteToken());
        filter.setDeviceOperatingSystemType(parameters.getDeviceOperatingSystemType());
        filter.setProviderType(parameters.getProviderType());
        filter.setStatus(parameters.getStatus());
        filter.setSubscriptionId(parameters.getSubscriptionId());
        filter.setDeviceUuId(parameters.getDeviceUuId());
        return filter;
    }

    /* Properties getters and setters */
    @Override
    public PushNotificationRecipientRepository getPushNotificationRecipientRepository() {
        return pushNotificationRecipientRepository;
    }

    @Override
    public void setPushNotificationRecipientRepository(final PushNotificationRecipientRepository pushNotificationRecipientRepository) {
        this.pushNotificationRecipientRepository = pushNotificationRecipientRepository;
    }
}
