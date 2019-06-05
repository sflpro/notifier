package com.sflpro.notifier.worker.resources.maintanance;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
@Path("heartbeat")
@Component
public class HeartBeatResource {

    @GET
    @Produces("text/plain")
    public String checkHeartBeat() {
        return "ALIVE";
    }
}
