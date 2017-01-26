package com.sfl.nms.api.internal.rest.resources.maintanance;

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
@Component
@Path("heartbeat")
public class HeartBeatResource {

    /* Constructors */
    public HeartBeatResource() {
    }

    @GET
    public Response checkHearBeat() {
        return Response.ok("OK").build();
    }
}
