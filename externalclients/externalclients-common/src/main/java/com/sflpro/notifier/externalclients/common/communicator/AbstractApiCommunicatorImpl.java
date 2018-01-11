package com.sflpro.notifier.externalclients.common.communicator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.externalclients.common.http.exception.ExternalClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/18/15
 * Time: 4:13 PM
 */
public class AbstractApiCommunicatorImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApiCommunicatorImpl.class);

    /* Constructors */
    public AbstractApiCommunicatorImpl() {
        LOGGER.debug("Initializing abstract API communicator");
    }

    protected <T> T deserializeJson(final String jsonString, final Class<T> resultClass) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final T result = objectMapper.readValue(jsonString, resultClass);
            LOGGER.debug("Successfully deSerialized jsonString - {} to object - {}", jsonString, result);
            return result;
        } catch (final Exception ex) {
            LOGGER.error("Error occurred while deSerializing JSON string - {}", jsonString, ex);
            throw new ExternalClientRuntimeException(ex);
        }
    }

    protected String serializeJson(final Object value) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String result = objectMapper.writeValueAsString(value);
            LOGGER.debug("Successfully serialized jsonString - {} to object - {}", result, result);
            return result;
        } catch (final Exception ex) {
            LOGGER.error("Error occurred while serializing JSON string - {}", value, ex);
            throw new ExternalClientRuntimeException(ex);
        }
    }
}
