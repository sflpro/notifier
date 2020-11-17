package com.sflpro.notifier.api.client.notification;

import com.sflpro.notifier.api.client.common.AbstractResourceClient;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

/**
 * Company: SFL LLC
 * Created on 16/11/2020
 *
 * @author Norik Aslanyan
 */
public class NotificationResourceClientImpl extends AbstractResourceClient implements NotificationResourceClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationResourceClientImpl.class);

    private static final String PATH = "/notification/";

    public NotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Override
    public NotificationModel get(final String id, final String authToken) {
        logger.trace("Getting NotificationModel for id:{}", id);
        final Invocation.Builder requestBuilder = getClient().target(getApiPath())
                .path(String.format("%s%s", PATH, id))
                .request(MediaType.APPLICATION_JSON_TYPE);
        if (authToken != null) {
            addAutorizationHeader(requestBuilder, authToken);
        }
        final NotificationModel notificationModel = requestBuilder.get(NotificationModel.class);
        logger.debug("Done getting NotificationModel for id:{}", id);
        return notificationModel;
    }
}
