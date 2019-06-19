package com.sflpro.notifier.services.notification.impl.component;

import com.sflpro.notifier.services.notification.component.SendEmailNotificationComponent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 11:56 AM
 */

@Component
public class SendEmailNotificationComponentImpl implements SendEmailNotificationComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailNotificationComponentImpl.class);

    /* Constructor */
    public SendEmailNotificationComponentImpl() {
        LOGGER.debug("Initializing send email notification component");
    }

    /* dependencies */
    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* public methods */
    @Override
    public void sendEmailNotification(final StartSendingNotificationEvent event) {
        applicationEventDistributionService.publishAsynchronousEvent(event);

    }
}
