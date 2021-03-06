package com.sflpro.notifier.api.facade.endpoints.notification;

import com.sflpro.notifier.api.facade.services.NotificationFacade;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Company: SFL LLC
 * Created on 16/11/2020
 *
 * @author Norik Aslanyan
 */
@SwaggerDefinition(tags = {@Tag(name = "notification", description = "Notification operations")})
@Api(tags = {"notification"})
@Singleton
@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

    @Inject
    private NotificationFacade notificationFacade;

    @ApiOperation("Get email notification")
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public NotificationModel get(@PathParam("id") final Long id) {
        Assert.notNull(id, "id can't be null");
        LOGGER.debug("Getting email notification:{}", id);
        final NotificationModel notification = notificationFacade.get(id);
        LOGGER.debug("Done getting email notification:{}", id);
        return notification;
    }
}
