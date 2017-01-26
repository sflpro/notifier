package com.sfl.nms.api.internal.rest.web.filter;

import com.sfl.nms.api.internal.rest.exception.AnonymisedApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/24/15
 * Time: 9:45 PM
 */
public class ApplicationExceptionHandlingFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandlingFilter.class);

    /* Constructors */
    public ApplicationExceptionHandlingFilter() {

    }

    /* Public method overrides */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("Initializing filter with name - {} and config parameter names - {}", filterConfig.getFilterName(), filterConfig.getInitParameterNames());
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);

        } catch (final Exception ex) {
            final String errorUuid = handleExceptionAndReturnUuId(ex);
            throw new AnonymisedApplicationException(errorUuid);
        }
    }

    @Override
    public void destroy() {
        LOGGER.debug("Destroying application exception filter - {}", this);
    }


    /* Utility methods */
    private String createErrorUuId() {
        return UUID.randomUUID().toString();
    }

    private String handleExceptionAndReturnUuId(final Exception ex) {
        final String errorUuid = createErrorUuId();
        LOGGER.error("Exception with UUID - " + errorUuid + " caught while processing the request. Original exception:", ex);
        return errorUuid;
    }
}
