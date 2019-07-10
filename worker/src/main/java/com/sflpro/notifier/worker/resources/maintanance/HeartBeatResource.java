package com.sflpro.notifier.worker.resources.maintanance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */

@RestController
public class HeartBeatResource {

    @GetMapping(path = "heartbeat")
    public String checkHeartBeat() {
        return "ALIVE";
    }
}
