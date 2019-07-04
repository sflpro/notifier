package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.push.sns.PushNotificationSnsRecipientService;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:10 AM
 */
@Component
class PushNotificationUserDeviceTokenProcessorImpl implements PushNotificationUserDeviceTokenProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationUserDeviceTokenProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    @Autowired
    private PushMessageServiceProvider pushMessageServiceProvider;

    @Autowired
    private ArnConfigurationService arnConfigurationService;

    /* Constructors */
    PushNotificationUserDeviceTokenProcessorImpl() {
        LOGGER.debug("Initializing push notification user device token SNS processor");
    }

    @Override
    public String registerUserDeviceToken(
            @Nonnull final String userDeviceToken,
            @Nonnull final DeviceOperatingSystemType operatingSystemType,
            @Nonnull final String applicationType,
            @Nullable final String currentProviderToken,
            @Nonnull final PushNotificationProviderType pushNotificationProviderType) {
        Assert.notNull(userDeviceToken, "User device token should not be null");
        Assert.notNull(operatingSystemType, "Operating system type should not be null");
        Assert.notNull(applicationType, "Push notification application type should not be null");
        Assert.notNull(pushNotificationProviderType, "Push notification provider type should not be null");
        LOGGER.debug("Registering user device token - {} , operating system type - {}", userDeviceToken, operatingSystemType);
        final String applicationArn = arnConfigurationService.getApplicationArnForMobilePlatform(operatingSystemType, applicationType);
        final PushMessageSubscriber pushMessageSubscriber = pushMessageServiceProvider.lookupPushMessageSubscriber(pushNotificationProviderType)
                .orElseThrow(() -> new IllegalStateException(format("No subscriber was registered for type '%s'", pushNotificationProviderType)));
        final String deviceEndpointArn = StringUtils.isEmpty(currentProviderToken) ? pushMessageSubscriber.registerDeviceEndpointArn(userDeviceToken, applicationArn) :
                pushMessageSubscriber.refreshDeviceEndpointArn(currentProviderToken, userDeviceToken, applicationArn);
        LOGGER.debug("Successfully registered user device token - {}, result endpoint ARN is - {}, platform - {}", userDeviceToken, deviceEndpointArn, operatingSystemType);
        return deviceEndpointArn;
    }

    @Override
    public PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final String recipientRouteToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType) {
        Assert.notNull(subscriptionId, "Push notification subscription should not be null");
        Assert.notNull(recipientRouteToken, "Recipient route token should not be null");
        Assert.notNull(operatingSystemType, "Mobile device operating system type should not be null");
        Assert.notNull(applicationType, "Application type should not be null");
        LOGGER.debug("Creating push notification SNS recipient for subscription with id - {}, recipient route token - {}, operating system type - {}", subscriptionId, recipientRouteToken, operatingSystemType);
        // Create push notification SNS recipient DTO
        final String platformApplicationArn = arnConfigurationService.getApplicationArnForMobilePlatform(operatingSystemType, applicationType);
        final PushNotificationSnsRecipientDto snsRecipientDto = new PushNotificationSnsRecipientDto();
        snsRecipientDto.setPlatformApplicationArn(platformApplicationArn);
        snsRecipientDto.setDestinationRouteToken(recipientRouteToken);
        snsRecipientDto.setDeviceOperatingSystemType(operatingSystemType);
        snsRecipientDto.setApplicationType(applicationType);
        // Create recipient
        final PushNotificationSnsRecipient recipient = pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, snsRecipientDto);
        LOGGER.debug("Successfully created push notification SNS recipient for subscription with id - {}, recipient route token - {}, operating system type - {}. Recipient - {}", subscriptionId, recipientRouteToken, operatingSystemType, recipient);
        return recipient;
    }

    /* Utility methods */
    /* Properties getters and setters */
    public PushNotificationSnsRecipientService getPushNotificationSnsRecipientService() {
        return pushNotificationSnsRecipientService;
    }

    public void setPushNotificationSnsRecipientService(final PushNotificationSnsRecipientService pushNotificationSnsRecipientService) {
        this.pushNotificationSnsRecipientService = pushNotificationSnsRecipientService;
    }

    public ArnConfigurationService getArnConfigurationService() {
        return arnConfigurationService;
    }

    public void setArnConfigurationService(final ArnConfigurationService arnConfigurationService) {
        this.arnConfigurationService = arnConfigurationService;
    }
}
