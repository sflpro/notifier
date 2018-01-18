package com.sflpro.notifier.services.notification.impl.push.sns;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.device.model.mobile.DeviceOperatingSystemType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Component;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 5:10 PM
 */
@Component
public class SnsArnConfigurationServiceImpl implements SnsArnConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnsArnConfigurationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PropertiesFactoryBean propertiesFactoryBean;

    /* Constructors */
    public SnsArnConfigurationServiceImpl() {
    }

    @Override
    public String getApplicationArnForMobilePlatform(final DeviceOperatingSystemType operatingSystemType, final String applicationType) {
        // Build application key
        final String arnPropertyKey = "amazon.account.sns.application.arn." + applicationType + "." + operatingSystemType.name().toLowerCase();
        // Grab application ARN
        String applicationArn = null;
        try {
            applicationArn = propertiesFactoryBean.getObject().getProperty(arnPropertyKey);
        } catch (final Exception ex) {
            LOGGER.error("Error occurred while getting application ARN for key - {}", arnPropertyKey);
            throw new ServicesRuntimeException(ex);
        }
        if (StringUtils.isBlank(applicationArn)) {
            final String message = "No application ARN was found for key - " + arnPropertyKey;
            LOGGER.error(message);
            throw new ServicesRuntimeException(message);
        }
        return applicationArn;
    }
}
