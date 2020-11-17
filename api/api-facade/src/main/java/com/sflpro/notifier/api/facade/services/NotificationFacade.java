package com.sflpro.notifier.api.facade.services;

import com.sflpro.notifier.api.model.notification.NotificationModel;

/**
 * Company: SFL LLC
 * Created on 17/11/2020
 *
 * @author Norik Aslanyan
 */
public interface NotificationFacade {

    NotificationModel get(Long id);
}
