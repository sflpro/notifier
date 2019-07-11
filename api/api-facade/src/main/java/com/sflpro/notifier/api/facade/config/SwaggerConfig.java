package com.sflpro.notifier.api.facade.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@Configuration
@ConditionalOnProperty(name = "notification.api.cors.allowSwagger", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public BeanConfig getSwaggerConfig() {
        final String version = getClass().getPackage().getImplementationVersion();

        final BeanConfig beanConfig = new BeanConfig();

        beanConfig.setVersion(version);

        beanConfig.setResourcePackage("com.sflpro.notifier.api.internal.rest.resources");
        beanConfig.setScan(true);

        final Info info = new Info();

        info.setTitle("Weadapt Notification API Documentation");
        info.setDescription("Description of API calls and their parameters");
        info.setContact(
                new Contact()
                        .name("Weadapt")
                        .email("info@weadapt.eu")
                        .url("https://weadapt.digital/contact/")
        );
        info.version(version);

        beanConfig.setInfo(info);

        return beanConfig;
    }
}
