package com.sflpro.notifier.queue.consumer.application.config;


import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
public class JerseyConfig extends ResourceConfig {

    @Autowired
    private ApplicationContext applicationContext;

    public JerseyConfig() {
        register(RequestContextFilter.class);
        packages("com.sflpro.notifier.queue.consumer.application");
        register(LoggingFilter.class);
    }

    @PostConstruct
    public void setup() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Path.class);
        for (Object o : beans.values()) {
            register(o);
        }
    }
}
