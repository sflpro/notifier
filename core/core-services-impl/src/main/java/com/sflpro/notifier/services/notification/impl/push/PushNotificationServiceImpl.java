package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.model.UserNotification;
import com.sflpro.notifier.db.entities.notification.push.*;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.user.UserService;
import com.sflpro.notifier.services.user.model.User;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    /* Dependencies */
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

    /* Constructors */
    public PushNotificationServiceImpl() {
        LOGGER.debug("Initializing push notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotification createNotification(@Nonnull final Long pushNotificationRecipientId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        Assert.notNull(pushNotificationRecipientId, "Push notification recipient id should not be null");
        assertPushNotificationDto(pushNotificationDto);
        assertPushNotificationProperties(pushNotificationPropertyDTOs);
        LOGGER.debug("Creating push notification for recipient with id - {}, DTO - {}, properties - {}", pushNotificationRecipientId, pushNotificationDto, pushNotificationPropertyDTOs);
        final PushNotificationRecipient recipient = pushNotificationRecipientService.getPushNotificationRecipientById(pushNotificationRecipientId);
        // Create push notification
        PushNotification pushNotification = new PushNotification(true);
        // Set properties
        pushNotificationDto.updateDomainEntityProperties(pushNotification);
        pushNotification.setRecipient(recipient);
        pushNotification.setProviderType(recipient.getType().getNotificationProviderType());
        // Create push notifications properties
        createAndAddPushNotificationProperties(pushNotification, pushNotificationPropertyDTOs);
        // Persist push notification
        pushNotification = pushNotificationRepository.save(pushNotification);
        LOGGER.debug("Successfully created push notification with id - {}, push notification - {}", pushNotification.getId(), pushNotification);
        return pushNotification;
    }

    @Transactional
    @Nonnull
    @Override
    public List<PushNotification> createNotificationsForUserActiveRecipients(@Nonnull final Long userId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        Assert.notNull(userId, "User id should not be null");
        assertPushNotificationDto(pushNotificationDto);
        assertPushNotificationProperties(pushNotificationPropertyDTOs);
        LOGGER.debug("Creating push notifications for all active recipients of user with id - {}, push notification DTO - {}", userId, pushNotificationDto);
        // Grab user and check if subscription exists
        final User user = userService.getUserById(userId);
        // Check if subscription exists for users
        final boolean subscriptionExists = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        if (!subscriptionExists) {
            LOGGER.debug("No push notification subscription exists for user with id - {}, do not create any notifications", user.getId());
            return Collections.emptyList();
        }
        // Grab subscription and search for recipients
        final PushNotificationSubscription subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(userId);
        final List<PushNotificationRecipient> recipients = getPushNotificationActiveRecipientsForSubscription(subscription);
        // Create push notifications
        final List<PushNotification> pushNotifications = createPushNotificationsForRecipients(recipients, user, pushNotificationDto, pushNotificationPropertyDTOs);
        LOGGER.debug("{} push notifications were created for user with id - {}, push notification DTO - {}", pushNotifications.size(), userId, pushNotificationDto);
        return pushNotifications;
    }

    /* Utility methods */
    private void assertPushNotificationProperties(final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        Assert.notNull(pushNotificationPropertyDTOs, "Push notification properties should not be null");
    }

    private void createAndAddPushNotificationProperties(final PushNotification pushNotification, final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        pushNotificationPropertyDTOs.forEach(pushNotificationPropertyDTO -> {
            // Assert push notification property DTO
            assertPushNotificationPropertyDto(pushNotificationPropertyDTO);
            // Create push notification property and set values
            final PushNotificationProperty pushNotificationProperty = new PushNotificationProperty();
            // Update properties
            pushNotificationPropertyDTO.updateDomainEntityProperties(pushNotificationProperty);
            // Build up relation between property and push notification
            pushNotificationProperty.setPushNotification(pushNotification);
            pushNotification.getProperties().add(pushNotificationProperty);
        });
    }

    private List<PushNotification> createPushNotificationsForRecipients(final List<PushNotificationRecipient> recipients, final User user, final PushNotificationDto pushNotificationDto, @Nonnull final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs) {
        // Create push notification recipients
        final List<PushNotification> pushNotifications = new ArrayList<>();
        recipients.forEach(recipient -> {
            final PushNotification pushNotification = createNotification(recipient.getId(), pushNotificationDto, pushNotificationPropertyDTOs);
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), pushNotification.getId(), new UserNotificationDto());
            LOGGER.debug("Created push notification with id - {} and corresponding user notification for id - {}", pushNotification.getId(), userNotification.getId());
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
        LOGGER.debug("{} recipients were found for push notification subscription with id - {}", recipients.size(), subscription.getId());
        return recipients;
    }

    private void assertPushNotificationDto(final PushNotificationDto pushNotificationDto) {
        assertNotificationDto(pushNotificationDto);
    }

    private void assertPushNotificationPropertyDto(final PushNotificationPropertyDto propertyDto) {
        Assert.notNull(propertyDto, "Push notification property DTO should not be null");
        Assert.notNull(propertyDto.getPropertyKey(), "Property key in push notification property DTO should not be null");
        Assert.notNull(propertyDto.getPropertyValue(), "Property key in push notification property DTO should not be null");
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
