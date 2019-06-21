package com.sflpro.notifier.api.facade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@Component
public class CorsFilter implements ContainerResponseFilter {

    @Value("${notification.api.cors.allowAll}")
    private boolean allowAll;

    @Value("${notification.api.cors.allowSwagger}")
    private boolean allowSwagger;

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        if(allowAll || (allowSwagger && request.getUriInfo().getPath().startsWith("swagger.json"))) {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
    }
}