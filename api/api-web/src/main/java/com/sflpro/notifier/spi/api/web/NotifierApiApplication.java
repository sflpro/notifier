package com.sflpro.notifier.api.web;

import com.sflpro.notifier.api.facade.config.ApplicationExceptionHandlingFilter;
import com.sflpro.notifier.api.facade.config.JerseyConfig;
import com.sflpro.notifier.queue.producer.ProducerConfiguration;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Company: SFL LLC
 * Created on 03/12/2017
 *
 * @author Davit Harutyunyan
 */
@SpringBootApplication
@Import(ProducerConfiguration.class)
public class NotifierApiApplication {

    @Value("${server.servlet.context-path}")
    private String urlMapping;

    public static void main(String[] args) {
        new SpringApplicationBuilder(NotifierApiApplication.class).run(args);
    }

    @Bean
    public ServletRegistrationBean jerseyServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), urlMapping);
        // our rest resources will be available in the path /rest/*
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());

        return registration;
    }

    @Bean
    public FilterRegistrationBean exceptionHandlingFilterRegistration() {

        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ApplicationExceptionHandlingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("applicationExceptionHandlingFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean encodingFilterRegistration() {

        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CharacterEncodingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("encoding.filter");
        final HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("encoding", "UTF-8");
        registration.setInitParameters(initParameters);
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean entityManagerFilterRegistration() {

        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OpenEntityManagerInViewFilter());
        registration.addUrlPatterns("/*");
        registration.setName("openEntityManagerInViewFilter");
        final HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("entityManagerFactory", "entityManagerFactory");
        registration.setInitParameters(initParameters);
        registration.setOrder(3);
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR));
        return registration;
    }

}