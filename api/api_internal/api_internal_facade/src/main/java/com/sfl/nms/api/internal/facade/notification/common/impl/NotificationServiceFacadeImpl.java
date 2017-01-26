package com.sfl.nms.api.internal.facade.notification.common.impl;

import com.sfl.nms.api.internal.facade.notification.common.NotificationServiceFacade;
import com.sfl.nms.services.notification.email.EmailNotificationService;
import com.sfl.nms.services.notification.push.PushNotificationService;
import com.sfl.nms.services.notification.sms.SmsNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:14 PM
 */
@Component
public class NotificationServiceFacadeImpl implements NotificationServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public NotificationServiceFacadeImpl() {
    }
}
