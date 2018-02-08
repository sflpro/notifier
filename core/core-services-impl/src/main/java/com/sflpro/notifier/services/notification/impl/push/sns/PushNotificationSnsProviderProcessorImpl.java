package com.sflpro.notifier.services.notification.impl.push.sns;

import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.AmazonSNSPlatformType;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 3:36 PM
 */
@Component
public class PushNotificationSnsProviderProcessorImpl implements PushNotificationSnsProviderProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSnsProviderProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Value("${amazon.account.sns.development}")
    private boolean developmentMode;

    /* Constructors */
    public PushNotificationSnsProviderProcessorImpl() {
        LOGGER.debug("Initializing push notification SNS processor");
    }

    @Override
    public String processPushNotification(@Nonnull final PushNotification pushNotification) {
        Assert.notNull(pushNotification, "Push notification should not be null");
        final PushNotificationRecipient recipient = pushNotification.getRecipient();
        Assert.isTrue(PushNotificationProviderType.SNS.equals(recipient.getType()), "Push notification provider should be type of SNS");
        LOGGER.debug("Sending SNS push notification - {} , recipient - {}", pushNotification, recipient);
        final Map<String, String> pushNotificationAttributes = createPushNotificationAttributes(pushNotification);
        final AmazonSNSPlatformType amazonSNSPlatformType = getAmazonSNSPlatformType(pushNotification.getRecipient().getDeviceOperatingSystemType());
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(pushNotification.getSubject(), pushNotification.getContent(), pushNotificationAttributes, amazonSNSPlatformType);
        final SendPushNotificationResponse response = amazonSnsApiCommunicator.sendPushNotification(messageInformation, recipient.getDestinationRouteToken());
        LOGGER.debug("Got SNS response - {} when sending push notification - {} to recipient - {}", response, pushNotification, recipient);
        return response.getMessageId();
    }

    /* Utility methods */
    private AmazonSNSPlatformType getAmazonSNSPlatformType(final DeviceOperatingSystemType operatingSystemType) {
        switch (operatingSystemType) {
            case IOS:
                if (developmentMode) {
                    return AmazonSNSPlatformType.APNS_SANDBOX;
                } else {
                    return AmazonSNSPlatformType.APNS;
                }
            case ANDROID:
                return AmazonSNSPlatformType.GCM;
            default: {
                final String message = "Unsupported operating system type - " + operatingSystemType;
                throw new ServicesRuntimeException(message);
            }
        }
    }

    private Map<String, String> createPushNotificationAttributes(final PushNotification pushNotification) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        pushNotification.getProperties().forEach(pushNotificationProperty -> {
            // Add to the map of attributes
            attributes.put(pushNotificationProperty.getPropertyKey(), pushNotificationProperty.getPropertyValue());
        });
        return attributes;
    }

    /* Properties getters and setters */
    public AmazonSnsApiCommunicator getAmazonSnsApiCommunicator() {
        return amazonSnsApiCommunicator;
    }

    public void setAmazonSnsApiCommunicator(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        this.amazonSnsApiCommunicator = amazonSnsApiCommunicator;
    }

    public boolean isDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(boolean developmentMode) {
        this.developmentMode = developmentMode;
    }
}
