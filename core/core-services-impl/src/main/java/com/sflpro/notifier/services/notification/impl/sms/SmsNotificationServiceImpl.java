package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.sms.SmsNotificationRepository;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
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
 * Date: 3/21/15
 * Time: 8:07 PM
 */
@Service
public class SmsNotificationServiceImpl extends AbstractNotificationServiceImpl<SmsNotification> implements SmsNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationRepository smsNotificationRepository;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public SmsNotificationServiceImpl() {
        LOGGER.debug("Initializing SMS notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public SmsNotification createSmsNotification(@Nonnull final SmsNotificationDto smsNotificationDto, final List<NotificationPropertyDto> smsNotificationPropertyDtos) {
        assertSmsNotificationDto(smsNotificationDto);
        Assert.notNull(smsNotificationPropertyDtos, "Properties list should not be null.");
        LOGGER.debug("Creating SMS notification for DTO - {}", smsNotificationDto);
        SmsNotification smsNotification = new SmsNotification(true);
        smsNotificationDto.updateDomainEntityProperties(smsNotification);
        createAndAddSmsNotificationProperties(smsNotification, smsNotificationPropertyDtos);
        // Persist notification
        smsNotification = smsNotificationRepository.save(smsNotification);
        associateUserWithNotificationIfRequired(smsNotificationDto.getUserUuid(), smsNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(smsNotification.getId(), smsNotificationDto.getSecureProperties()));
        LOGGER.debug("Successfully created SMS notification with id - {}, notification - {}", smsNotification.getId(), smsNotification);
        return smsNotification;
    }

    /* Utility methods */
    private void assertSmsNotificationDto(final SmsNotificationDto notificationDto) {
        assertNotificationDto(notificationDto);
        Assert.notNull(notificationDto.getProviderType(), "ProviderType in notification DTO should not be null");
        Assert.notNull(notificationDto.getRecipientMobileNumber(), "Recipient mobile number in notification DTO should not be null");
    }

    @Override
    protected AbstractNotificationRepository<SmsNotification> getRepository() {
        return smsNotificationRepository;
    }

    @Override
    protected Class<SmsNotification> getInstanceClass() {
        return SmsNotification.class;
    }

    /* Properties getters and setters */
    public SmsNotificationRepository getSmsNotificationRepository() {
        return smsNotificationRepository;
    }

    public void setSmsNotificationRepository(final SmsNotificationRepository smsNotificationRepository) {
        this.smsNotificationRepository = smsNotificationRepository;
    }

    private static void createAndAddSmsNotificationProperties(final SmsNotification smsNotification, final List<NotificationPropertyDto> notificationPropertyDtos) {
        notificationPropertyDtos.forEach(propertyDto -> {
            assertEmailNotificationPropertyDto(propertyDto);
            final NotificationProperty smsNotificationProperty = new NotificationProperty();
            propertyDto.updateDomainEntityProperties(smsNotificationProperty);
            smsNotificationProperty.setNotification(smsNotification);
            smsNotification.getProperties().add(smsNotificationProperty);
        });
    }

    private static void assertEmailNotificationPropertyDto(final NotificationPropertyDto propertyDto) {
        Assert.notNull(propertyDto, "Notification property DTO should not be null");
        Assert.notNull(propertyDto.getPropertyKey(), "Property key in notification property DTO should not be null");
    }

}
