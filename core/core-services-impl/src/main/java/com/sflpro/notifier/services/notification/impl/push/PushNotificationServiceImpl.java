package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientsParameters;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:46 AM
 */
@Service
public class PushNotificationServiceImpl extends AbstractNotificationServiceImpl<PushNotification> implements PushNotificationService {

    //region Logger

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    //endregion

    //region Injections

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    @Autowired
    private UserService userService;

    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Autowired
    private UserNotificationService userNotificationService;

    //endregion

    //region Constants

    PushNotificationServiceImpl() {
        logger.debug("Initializing push notification service");
    }

    //endregion

    @Transactional
    @Nonnull
    @Override
    public PushNotification createNotification(@Nonnull final Long pushNotificationRecipientId, @Nonnull final PushNotificationDto pushNotificationDto) {
        Assert.notNull(pushNotificationRecipientId, "Push notification recipient id should not be null");
        assertPushNotificationDto(pushNotificationDto);
        logger.debug("Creating push notification for recipient with id - {}, DTO - {}, properties - {}", pushNotificationRecipientId, pushNotificationDto, pushNotificationDto.getProperties());
        final PushNotificationRecipient recipient = pushNotificationRecipientService.getPushNotificationRecipientById(pushNotificationRecipientId);
        // Create push notification
        PushNotification pushNotification = new PushNotification(true);
        // Set properties
        pushNotificationDto.updateDomainEntityProperties(pushNotification);
        pushNotification.setRecipient(recipient);
        pushNotification.setProviderType(recipient.getType().getNotificationProviderType());
        pushNotification.setLocale(pushNotificationDto.getLocale());
        pushNotification.setTemplateName(pushNotificationDto.getTemplateName());
        // Create push notifications properties
        // Persist push notification
        pushNotification = pushNotificationRepository.save(pushNotification);
        logger.debug("Successfully created push notification with id - {}, push notification - {}", pushNotification.getId(), pushNotification);
        return pushNotification;
    }

    @Transactional
    @Nonnull
    @Override
    public List<PushNotification> createNotificationsForRecipients(@Nonnull final PushNotificationRecipientsParameters recipientsParameters, @Nonnull final PushNotificationDto pushNotificationDto) {
        Assert.notNull(recipientsParameters, "Recipients parameters should not be null");
        Assert.notNull(recipientsParameters.getUserId(), "User id should not be null");
        assertPushNotificationDto(pushNotificationDto);
        logger.debug("Creating push notifications for recipients with parameters - {}, push notification DTO - {}", recipientsParameters, pushNotificationDto);
        // Grab user and check if subscription exists
        final User user = userService.getUserById(recipientsParameters.getUserId());
        // Check if subscription exists for users
        final boolean subscriptionExists = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        if (!subscriptionExists) {
            logger.debug("No push notification subscription exists for user with id - {}, do not create any notifications", user.getId());
            return Collections.emptyList();
        }
        // Grab subscription and search for recipients
        final PushNotificationSubscription subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(recipientsParameters.getUserId());
        final List<PushNotificationRecipient> recipients = getPushNotificationRecipientsForSubscription(subscription, recipientsParameters);
        // Create push notifications
        final List<PushNotification> pushNotifications = createPushNotificationsForRecipients(recipients, user, pushNotificationDto);
        logger.debug("{} push notifications were created for recipients - {}, push notification DTO - {}", pushNotifications.size(), recipientsParameters, pushNotificationDto);
        return pushNotifications;
    }

    @Override
    @Transactional(readOnly = true)
    public PushNotification getPushNotificationForProcessing(final Long notificationId) {
        Assert.notNull(notificationId, "notificationId id should not be null");
        return pushNotificationRepository.findByIdForProcessingFlow(notificationId);
    }

    /* Utility methods */

    private List<PushNotification> createPushNotificationsForRecipients(final List<PushNotificationRecipient> recipients, final User user, final PushNotificationDto pushNotificationDto) {
        // Create push notification recipients
        final List<PushNotification> pushNotifications = new ArrayList<>();
        recipients.forEach(recipient -> {
            final PushNotification pushNotification = createNotification(recipient.getId(), pushNotificationDto);
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), pushNotification.getId(), new UserNotificationDto());
            logger.debug("Created push notification with id - {} and corresponding user notification for id - {}", pushNotification.getId(), userNotification.getId());
            pushNotifications.add(pushNotification);
        });
        return pushNotifications;
    }

    private List<PushNotificationRecipient> getPushNotificationRecipientsForSubscription(final PushNotificationSubscription subscription, final PushNotificationRecipientsParameters recipientsParameters) {
        // Build search parameters
        final PushNotificationRecipientSearchParameters searchParameters = new PushNotificationRecipientSearchParameters();
        searchParameters.setSubscriptionId(subscription.getId());
        searchParameters.setDeviceUuId(recipientsParameters.getDeviceUuId());
        searchParameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        // Execute search
        final List<PushNotificationRecipient> recipients = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(searchParameters, 0L, Integer.MAX_VALUE);
        logger.debug("{} recipients were found for push notification subscription with id - {}", recipients.size(), subscription.getId());
        return recipients;
    }

    private void assertPushNotificationDto(final PushNotificationDto pushNotificationDto) {
        assertNotificationDto(pushNotificationDto);
    }

    @Override
    protected AbstractNotificationRepository<PushNotification> getRepository() {
        return pushNotificationRepository;
    }

    @Override
    protected Class<PushNotification> getInstanceClass() {
        return PushNotification.class;
    }

    /* Properties getters and setters */
    public PushNotificationRepository getPushNotificationRepository() {
        return pushNotificationRepository;
    }

    public void setPushNotificationRepository(final PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    public PushNotificationRecipientService getPushNotificationRecipientService() {
        return pushNotificationRecipientService;
    }

    public void setPushNotificationRecipientService(final PushNotificationRecipientService pushNotificationRecipientService) {
        this.pushNotificationRecipientService = pushNotificationRecipientService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public PushNotificationSubscriptionService getPushNotificationSubscriptionService() {
        return pushNotificationSubscriptionService;
    }

    public void setPushNotificationSubscriptionService(final PushNotificationSubscriptionService pushNotificationSubscriptionService) {
        this.pushNotificationSubscriptionService = pushNotificationSubscriptionService;
    }

    public UserNotificationService getUserNotificationService() {
        return userNotificationService;
    }

    public void setUserNotificationService(final UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }
}
