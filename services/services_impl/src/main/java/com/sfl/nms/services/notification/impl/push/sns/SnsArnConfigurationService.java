package com.sfl.nms.services.notification.impl.push.sns;

import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 5:10 PM
 */
public interface SnsArnConfigurationService {

    /**
     * Retrieves SNS ARN for provided mobile application
     *
     * @param operatingSystemType
     * @param applicationType
     * @return applicationArn
     */
    String getApplicationArnForMobilePlatform(final DeviceOperatingSystemType operatingSystemType, final String applicationType);
}
