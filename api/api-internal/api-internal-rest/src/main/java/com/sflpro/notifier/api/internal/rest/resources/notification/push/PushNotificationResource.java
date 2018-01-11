package com.sflpro.notifier.api.internal.rest.resources.notification.push;

import com.sflpro.notifier.api.internal.facade.notification.push.PushNotificationServiceFacade;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.core.api.internal.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.push.response.UpdatePushNotificationSubscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 12:40 PM
 */
@Component
@Path("notification/push")
@Produces("application/json")
public class PushNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationResource.class);

    /* Dependencies */
    @Autowired
    private PushNotificationServiceFacade pushNotificationServiceFacade;

    /* Constructors */
    public PushNotificationResource() {
        //default constructor
    }

    @POST
    @Path("create")
    @Consumes("application/json")
    public Response createPushNotification(final CreatePushNotificationRequest request) {
        LOGGER.debug("Processing create push notification request - {}", request);
        final ResultResponseModel<CreatePushNotificationResponse> response = pushNotificationServiceFacade.createPushNotifications(request);
        LOGGER.debug("Processed create push notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }

    @POST
    @Path("subscribe")
    @Consumes("application/json")
    public Response updatePushNotificationsSubscription(final UpdatePushNotificationSubscriptionRequest request) {
        LOGGER.debug("Processing create push notification subscription request - {}", request);
        final ResultResponseModel<UpdatePushNotificationSubscriptionResponse> response = pushNotificationServiceFacade.updatePushNotificationSubscription(request);
        LOGGER.debug("Processed create push notification subscription request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}
