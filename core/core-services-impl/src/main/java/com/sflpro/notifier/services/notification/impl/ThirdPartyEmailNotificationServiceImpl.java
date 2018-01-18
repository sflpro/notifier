package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.persistence.repositories.notification.email.ThirdPartyEmailNotificationRepository;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.email.ThirdPartyEmailNotificationService;
import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;
import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotificationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:24 PM
 */
@Service
public class ThirdPartyEmailNotificationServiceImpl extends AbstractNotificationServiceImpl<ThirdPartyEmailNotification> implements ThirdPartyEmailNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyEmailNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private ThirdPartyEmailNotificationRepository thirdPartyEmailNotificationRepository;

    /* Constructors */
    public ThirdPartyEmailNotificationServiceImpl() {
        LOGGER.debug("Initializing third party email notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public ThirdPartyEmailNotification createEmailNotification(@Nonnull final ThirdPartyEmailNotificationDto thirdPartyEmailNotificationDto, @Nonnull final List<ThirdPartyEmailNotificationPropertyDto> thirdPartyEmailNotificationPropertyDtos) {
        assertThirdPartyEmailNotificationDto(thirdPartyEmailNotificationDto);
        Assert.notNull(thirdPartyEmailNotificationPropertyDtos, "thirdPartyEmailNotificationPropertyDtos should not be null");
        LOGGER.debug("Creating third party email notification for DTO - {} and property DTOs", thirdPartyEmailNotificationDto, thirdPartyEmailNotificationPropertyDtos);
        ThirdPartyEmailNotification thirdPartyEmailNotification = new ThirdPartyEmailNotification(true);
        thirdPartyEmailNotificationDto.updateDomainEntityProperties(thirdPartyEmailNotification);
        // Create third party email notification properties
        createAndAddThirdPartyEmailNotificationProperties(thirdPartyEmailNotification, thirdPartyEmailNotificationPropertyDtos);
        // Persist notification
        thirdPartyEmailNotification = thirdPartyEmailNotificationRepository.save(thirdPartyEmailNotification);
        LOGGER.debug("Successfully created email notification with id - {}, email notification - {}", thirdPartyEmailNotification.getId(), thirdPartyEmailNotification);
        return thirdPartyEmailNotification;
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationRepository<ThirdPartyEmailNotification> getRepository() {
        return thirdPartyEmailNotificationRepository;
    }

    @Override
    protected Class<ThirdPartyEmailNotification> getInstanceClass() {
        return ThirdPartyEmailNotification.class;
    }

    private void assertThirdPartyEmailNotificationDto(final ThirdPartyEmailNotificationDto notificationDto) {
        assertNotificationDto(notificationDto);
        Assert.notNull(notificationDto.getProviderType(), "ProviderType in notification DTO should not be null");
        Assert.notNull(notificationDto.getRecipientEmail(), "Recipient email in notification DTO should not be null");
    }

    private static void createAndAddThirdPartyEmailNotificationProperties(final ThirdPartyEmailNotification thirdPartyEmailNotification, final List<ThirdPartyEmailNotificationPropertyDto> thirdPartyEmailNotificationPropertyDtos) {
        thirdPartyEmailNotificationPropertyDtos.forEach(thirdPartyEmailNotificationPropertyDto -> {
            // Assert third party email notification property DTO
            assertThirdPartyEmailNotificationPropertyDto(thirdPartyEmailNotificationPropertyDto);
            // Create third party email notification property and set values
            final ThirdPartyEmailNotificationProperty thirdPartyEmailNotificationProperty = new ThirdPartyEmailNotificationProperty();
            // Update properties
            thirdPartyEmailNotificationPropertyDto.updateDomainEntityProperties(thirdPartyEmailNotificationProperty);
            // Build up relation between property and push notification
            thirdPartyEmailNotificationProperty.setThirdPartyEmailNotification(thirdPartyEmailNotification);
            thirdPartyEmailNotification.getProperties().add(thirdPartyEmailNotificationProperty);
        });
    }

    private static void assertThirdPartyEmailNotificationPropertyDto(final ThirdPartyEmailNotificationPropertyDto propertyDto) {
        Assert.notNull(propertyDto, "Third party email notification property DTO should not be null");
        Assert.notNull(propertyDto.getPropertyKey(), "Property key in third party email notification property DTO should not be null");
    }

    /* Properties getters and setters */
    public ThirdPartyEmailNotificationRepository getThirdPartyEmailNotificationRepository() {
        return thirdPartyEmailNotificationRepository;
    }

    public void setThirdPartyEmailNotificationRepository(final ThirdPartyEmailNotificationRepository thirdPartyEmailNotificationRepository) {
        this.thirdPartyEmailNotificationRepository = thirdPartyEmailNotificationRepository;
    }
}
