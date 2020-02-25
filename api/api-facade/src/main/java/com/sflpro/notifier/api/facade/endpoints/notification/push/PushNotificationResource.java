package com.sflpro.notifier.api.facade.endpoints.notification.push;

import com.sflpro.notifier.api.facade.services.push.PushNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
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
@SwaggerDefinition(tags = {@Tag(name = "push", description = "Push-notification operations")})
@Api(tags = {"push"})
@Singleton
@Path("notification/push")
@Produces("application/json")
public class PushNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationResource.class);

    /* Dependencies */
    @Inject
    private PushNotificationServiceFacade pushNotificationServiceFacade;

    /* Constructors */
    public PushNotificationResource() {
        //default constructor
    }

    @ApiOperation("Creates push notification")
    @POST
    @Path("create")
    @Consumes("application/json")
    public Response createPushNotification(final CreatePushNotificationRequest request) {
        LOGGER.debug("Processing create push notification request - {}", request);
        final ResultResponseModel<CreatePushNotificationResponse> response = pushNotificationServiceFacade.createPushNotifications(request);
        LOGGER.debug("Processed create push notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }

    @ApiOperation("Subscribes for push notifications")
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
