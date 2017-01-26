package com.sfl.nms.services.notification.impl.push.sns;

import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.impl.push.AbstractPushNotificationRecipientServiceImpl;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.services.notification.push.sns.PushNotificationSnsRecipientService;
import com.sfl.nms.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sfl.nms.persistence.repositories.notification.push.sns.PushNotificationSnsRecipientRepository;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipientStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:17 PM
 */
@Service
public class PushNotificationSnsRecipientServiceImpl extends AbstractPushNotificationRecipientServiceImpl<PushNotificationSnsRecipient> implements PushNotificationSnsRecipientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSnsRecipientServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSnsRecipientRepository pushNotificationSnsRecipientRepository;

    /* Constructors */
    public PushNotificationSnsRecipientServiceImpl() {
        LOGGER.debug("Initializing push notification SNS recipient service");
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationSnsRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final PushNotificationSnsRecipientDto recipientDto) {
        assertPushNotificationSubscriptionIdNotNull(subscriptionId);
        assertPushNotificationSnsRecipientDto(recipientDto);
        LOGGER.debug("Creating push notification recipient DTO for subscription with id - {}, DTO - {}", subscriptionId, recipientDto);
        final PushNotificationSubscription subscription = getPushNotificationSubscriptionService().getPushNotificationSubscriptionById(subscriptionId);
        assertNoRecipientExists(recipientDto.getType(), subscription, recipientDto.getDestinationRouteToken(), recipientDto.getApplicationType());
        // Create new push notification recipient
        PushNotificationSnsRecipient recipient = new PushNotificationSnsRecipient(true);
        recipientDto.updateDomainEntityProperties(recipient);
        recipient.setStatus(PushNotificationRecipientStatus.ENABLED);
        recipient.setSubscription(subscription);
        // Persist recipient
        recipient = pushNotificationSnsRecipientRepository.save(recipient);
        LOGGER.debug("Successfully created push notification recipient for id - {}, recipient - {}", recipient.getId(), recipient);
        return recipient;
    }

    /* Utility methods */
    protected void assertPushNotificationSnsRecipientDto(final PushNotificationSnsRecipientDto recipientDto) {
        assertPushNotificationRecipientDto(recipientDto);
        Assert.notNull(recipientDto.getPlatformApplicationArn(), "Platform application ARN should not be null in SNS recipient DTO");
    }

    @Override
    protected AbstractPushNotificationRecipientRepository<PushNotificationSnsRecipient> getRepository() {
        return pushNotificationSnsRecipientRepository;
    }

    @Override
    protected Class<PushNotificationSnsRecipient> getInstanceClass() {
        return PushNotificationSnsRecipient.class;
    }

    /* Properties getters and setters */
    public PushNotificationSnsRecipientRepository getPushNotificationSnsRecipientRepository() {
        return pushNotificationSnsRecipientRepository;
    }

    public void setPushNotificationSnsRecipientRepository(final PushNotificationSnsRecipientRepository pushNotificationSnsRecipientRepository) {
        this.pushNotificationSnsRecipientRepository = pushNotificationSnsRecipientRepository;
    }
}
