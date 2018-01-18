package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientRepository;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        final List<PushNotificationRecipient> recipients = pushNotificationRecipientRepository.findPushNotificationRecipients(searchParameters, startFrom, maxCount);
        LOGGER.debug("Got {} recipients for search parameters - {}, start from - {}, max count - {}", recipients.size(), searchParameters, startFrom, maxCount);
        return recipients;
    }

    @Nonnull
    @Override
    public Long getPushNotificationRecipientsCountForSearchParameters(@Nonnull final PushNotificationRecipientSearchParameters searchParameters) {
        assertSearchParameters(searchParameters);
        LOGGER.debug("Getting push notification recipients count for payment search parameters - {}", searchParameters);
        final Long count = pushNotificationRecipientRepository.getPushNotificationRecipientsCount(searchParameters);
        LOGGER.debug("Found - {} push notification recipients for search parameters - {}", count, searchParameters);
        return count;
    }

    /* Utility methods */
    @Override
    protected AbstractPushNotificationRecipientRepository<PushNotificationRecipient> getRepository() {
        return pushNotificationRecipientRepository;
    }

    @Override
    protected Class<PushNotificationRecipient> getInstanceClass() {
        return PushNotificationRecipient.class;
    }

    private void assertSearchParameters(final PushNotificationRecipientSearchParameters searchParameters) {
        Assert.notNull(searchParameters, "Search parameters should not be null");
    }

    /* Properties getters and setters */
    public PushNotificationRecipientRepository getPushNotificationRecipientRepository() {
        return pushNotificationRecipientRepository;
    }

    public void setPushNotificationRecipientRepository(final PushNotificationRecipientRepository pushNotificationRecipientRepository) {
        this.pushNotificationRecipientRepository = pushNotificationRecipientRepository;
    }
}
