package com.sflpro.notifier.api.facade.services;

import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.services.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Company: SFL LLC
 * Created on 17/11/2020
 *
 * @author Norik Aslanyan
 */
@Service
public class NotificationFacadeImpl implements NotificationFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationFacadeImpl.class);

    @Autowired
    private NotificationService notificationService;

    @Transactional(readOnly = true)
    @Override
    public NotificationModel get(Long id) {
        LOGGER.trace("Getting notification with id:{}", id);
        final Notification notification = notificationService.getNotificationById(id);
        final NotificationModel notificationModel = NotificationConverterHelper.convert(notification);
        LOGGER.debug("Done getting notification with id:{}", id);
        return notificationModel;
    }
}
