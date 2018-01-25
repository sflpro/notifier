package com.sflpro.notifier.api.internal.rest.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/8/16
 * Time 6:19 PM
 */
public class GenericJerseyConfig extends ResourceConfig {
    public GenericJerseyConfig() {
        // Configurations
        register(RequestContextFilter.class);
        register(JacksonJsonProvider.class);

        // Swagger specific settings
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        register(CorsFilter.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
    }
}
