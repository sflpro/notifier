package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
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
import com.sflpro.notifier.services.notification.dto.push.TemplatedPushNotificationDto;
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
import java.util.stream.Collectors;

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
        // Create push notifications properties
        // Persist push notification
        pushNotification = pushNotificationRepository.save(pushNotification);
        logger.debug("Successfully created push notification with id - {}, push notification - {}", pushNotification.getId(), pushNotification);
        return pushNotification;
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotification createNotification(@Nonnull final Long pushNotificationRecipientId, @Nonnull final TemplatedPushNotificationDto templatedPushNotificationDto) {
        Assert.notNull(pushNotificationRecipientId, "Push notification recipient id should not be null");
        assertTemplatedPushNotificationDto(templatedPushNotificationDto);
        logger.debug("Creating templated push notification for recipient with id - {}, DTO - {}, properties - {}", pushNotificationRecipientId, templatedPushNotificationDto, templatedPushNotificationDto.getProperties());
        final PushNotificationRecipient recipient = pushNotificationRecipientService.getPushNotificationRecipientById(pushNotificationRecipientId);
        // Create push notification
        PushNotification pushNotification = new PushNotification(true);
        // Set properties
        pushNotification.setType(templatedPushNotificationDto.getType());
        pushNotification.setClientIpAddress(templatedPushNotificationDto.getClientIpAddress());
        pushNotification.setTemplateName(templatedPushNotificationDto.getTemplateName());
        pushNotification.setRecipient(recipient);
        pushNotification.setProviderType(recipient.getType().getNotificationProviderType());
        pushNotification.setLocale(templatedPushNotificationDto.getLocale());
        pushNotification.setProperties(templatedPushNotificationDto.getProperties().entrySet()
                .stream()
                .map(entry -> new NotificationProperty(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        // Create push notifications properties
        // Persist push notification
        pushNotification = pushNotificationRepository.save(pushNotification);
        logger.debug("Successfully templated created push notification with id - {}, push notification - {}", pushNotification.getId(), pushNotification);
        return pushNotification;
    }

    @Transactional
    @Nonnull
    @Override
    public List<PushNotification> createNotificationsForUserActiveRecipients(@Nonnull final Long userId, @Nonnull final PushNotificationDto pushNotificationDto) {
        Assert.notNull(userId, "User id should not be null");
        assertPushNotificationDto(pushNotificationDto);
        logger.debug("Creating push notifications for all active recipients of user with id - {}, push notification DTO - {}", userId, pushNotificationDto);
        // Grab user and check if subscription exists
        final User user = userService.getUserById(userId);
        // Check if subscription exists for users
        final boolean subscriptionExists = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        if (!subscriptionExists) {
            logger.debug("No push notification subscription exists for user with id - {}, do not create any notifications", user.getId());
            return Collections.emptyList();
        }
        // Grab subscription and search for recipients
        final PushNotificationSubscription subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(userId);
        final List<PushNotificationRecipient> recipients = getPushNotificationActiveRecipientsForSubscription(subscription);
        // Create push notifications
        final List<PushNotification> pushNotifications = createPushNotificationsForRecipients(recipients, user, pushNotificationDto);
        logger.debug("{} push notifications were created for user with id - {}, push notification DTO - {}", pushNotifications.size(), userId, pushNotificationDto);
        return pushNotifications;
    }

    @Nonnull
    @Override
    public List<PushNotification> createNotificationsForUserActiveRecipients(@Nonnull final Long userId, @Nonnull final TemplatedPushNotificationDto templatedPushNotificationDto) {
        Assert.notNull(userId, "User id should not be null");
        assertTemplatedPushNotificationDto(templatedPushNotificationDto);

        logger.debug("Creating templated push notifications for all active recipients of user with id - {}, push TemplatedPushNotificationDto - {}", userId, templatedPushNotificationDto);
        // Grab user and check if subscription exists
        final User user = userService.getUserById(userId);
        // Check if subscription exists for users
        final boolean subscriptionExists = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        if (!subscriptionExists) {
            logger.debug("No push notification subscription exists for user with id - {}, do not create any notifications", user.getId());
            return Collections.emptyList();
        }
        // Grab subscription and search for recipients
        final PushNotificationSubscription subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(userId);
        final List<PushNotificationRecipient> recipients = getPushNotificationActiveRecipientsForSubscription(subscription);
        // Create push notifications
        final List<PushNotification> pushNotifications = createPushNotificationsForRecipients(recipients, user, templatedPushNotificationDto);
        logger.debug("{} push notifications were created for user with id - {}, push notification TemplatedPushNotificationDto - {}", pushNotifications.size(), userId, templatedPushNotificationDto);
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

    private List<PushNotification> createPushNotificationsForRecipients(final List<PushNotificationRecipient> recipients, final User user, final TemplatedPushNotificationDto templatedPushNotificationDto) {
        // Create push notification recipients
        final List<PushNotification> pushNotifications = new ArrayList<>();
        recipients.forEach(recipient -> {
            final PushNotification pushNotification = createNotification(recipient.getId(), templatedPushNotificationDto);
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), pushNotification.getId(), new UserNotificationDto());
            logger.debug("Created templated push notification with id - {} and corresponding user notification for id - {}", pushNotification.getId(), userNotification.getId());
            pushNotifications.add(pushNotification);
        });
        return pushNotifications;
    }

    private List<PushNotificationRecipient> getPushNotificationActiveRecipientsForSubscription(final PushNotificationSubscription subscription) {
        // Build search parameters
        final PushNotificationRecipientSearchParameters searchParameters = new PushNotificationRecipientSearchParameters();
        searchParameters.setSubscriptionId(subscription.getId());
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

    private static void assertTemplatedPushNotificationDto(final TemplatedPushNotificationDto templatedPushNotificationDto) {
        Assert.notNull(templatedPushNotificationDto, "TemplatedPushNotificationDto should not be null");
        Assert.notNull(templatedPushNotificationDto.getType(), "TemplatedPushNotificationDto.type should not be null");
        Assert.notNull(templatedPushNotificationDto.getClientIpAddress(), "TemplatedPushNotificationDto.clientIpAddress should not be null/empty");
        Assert.notNull(templatedPushNotificationDto.getTemplateName(), "TemplatedPushNotificationDto.templateName should not be null/empty");
        Assert.notNull(templatedPushNotificationDto.getProviderType(), "TemplatedPushNotificationDto.providerType should not be null");
    }
}
