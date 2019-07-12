package com.sflpro.notifier.api.facade.config;

import com.sflpro.notifier.api.facade.endpoints.maintanance.HeartBeatResource;
import com.sflpro.notifier.api.facade.endpoints.notification.email.EmailNotificationResource;
import com.sflpro.notifier.api.facade.endpoints.notification.push.PushNotificationResource;
import com.sflpro.notifier.api.facade.endpoints.notification.sms.SmsNotificationResource;
import com.sflpro.notifier.api.facade.security.PermissionDeniedException;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextListener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Collections;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@Configuration
@ApplicationPath("/*")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        super();
        // Swagger specific settings
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        register(CorsFilter.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

        // Endpoints
        register(HeartBeatResource.class);
        register(EmailNotificationResource.class);
        register(SmsNotificationResource.class);
        register(PushNotificationResource.class);
        // Features
        register(LoggingFeature.class);
        register(PermissionDeniedExceptionMapper.class);
    }

    @Bean
    @Lazy(false)
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    private static class PermissionDeniedExceptionMapper implements ExceptionMapper<PermissionDeniedException> {
        @Override
        public Response toResponse(final PermissionDeniedException exception) {
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), exception.getMessage())
                    .entity(Collections.singletonMap("message", "Unauthorized access!"))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }
}
