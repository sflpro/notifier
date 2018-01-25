package com.sflpro.notifier.queue.consumer.application;

import com.sflpro.notifier.queue.consumer.application.config.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.sflpro.notifier.queue.consumer")
@ImportResource({"applicationContext-queue-consumer.xml", "classpath:applicationContext-services.xml"})
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerApplication.class).run(args);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rest/*");
        // Our rest resources will be available in the path /rest/*
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }
}
