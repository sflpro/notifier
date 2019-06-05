package com.sflpro.notifier.worker;

import com.sflpro.notifier.queue.consumer.ConsumerConfiguration;
import com.sflpro.notifier.services.NotifierServicesConfiguration;
import com.sflpro.notifier.worker.config.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
@SpringBootApplication
@Import({NotifierServicesConfiguration.class, ConsumerConfiguration.class})
public class NotifierWorkerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NotifierWorkerApplication.class).run(args);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rest/*");
        // Our rest resources will be available in the path /rest/*
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }
}
