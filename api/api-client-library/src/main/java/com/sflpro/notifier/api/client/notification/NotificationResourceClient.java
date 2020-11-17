package com.sflpro.notifier.api.client.notification;

import com.sflpro.notifier.api.model.notification.NotificationModel;

import javax.annotation.Nonnull;

/**
 * Company: SFL LLC
 * Created on 16/11/2020
 *
 * @author Norik Aslanyan
 */
public interface NotificationResourceClient {

    /**
     * Create email notification
     *
     * @param id
     * @param authToken The Bearer token to be used for authentication/authorization
     * @return response
     */
    @Nonnull
    NotificationModel get(@Nonnull final String id, @Nonnull final String authToken);
}
