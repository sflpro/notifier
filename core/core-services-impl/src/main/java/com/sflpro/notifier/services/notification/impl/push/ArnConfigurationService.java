package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 5:10 PM
 */
public interface ArnConfigurationService {

    /**
     * Retrieves SNS ARN for provided mobile application
     *
     * @param operatingSystemType
     * @param applicationType
     * @return applicationArn
     */
    String getApplicationArnForMobilePlatform(final DeviceOperatingSystemType operatingSystemType, final String applicationType);
}
