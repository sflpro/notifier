package com.sflpro.notifier.api.facade.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sflpro.notifier.api.facade.endpoints.maintanance.HeartBeatResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        super();

        // Configurations
        register(RequestContextFilter.class);
        register(JacksonJsonProvider.class);

        // Swagger specific settings
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        register(CorsFilter.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

        // Endpoints
        register(HeartBeatResource.class);

        // Filters
        register(LoggingFilter.class);
        register(JerseyConfig.class);
    }

}
