package com.sflpro.notifier.api.internal.rest.resources.maintanance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 5/19/15
 * Time: 1:32 AM
 */
@SwaggerDefinition(tags = {@Tag(name = "heartbeat", description = "The status of the notification service")})
@Api(tags = {"heartbeat"})
@Component
@Path("heartbeat")
public class HeartBeatResource {

    /* Constructors */
    public HeartBeatResource() {
        //default constructor
    }

    @ApiOperation("Returns notification service's status")
    @GET
    public Response checkHearBeat() {
        return Response.ok("OK").build();
    }
}
