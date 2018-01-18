package com.sflpro.notifier.api.internal.facade.notification.common.impl;

import com.sflpro.notifier.api.internal.facade.notification.common.NotificationServiceFacade;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
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

    /* Dependencies */
    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public NotificationServiceFacadeImpl() {
        //default constructor
    }
}
