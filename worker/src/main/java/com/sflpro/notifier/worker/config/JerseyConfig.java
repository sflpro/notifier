package com.sflpro.notifier.worker.config;


import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
@Component
@ApplicationPath("/*")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        packages("com.sflpro.notifier.queue.consumer.notification");
        register(LoggingFeature.class);
    }
}
