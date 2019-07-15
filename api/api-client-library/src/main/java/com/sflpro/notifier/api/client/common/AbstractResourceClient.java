package com.sflpro.notifier.api.client.common;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/19/15
 * Time: 11:33 AM
 */
public class AbstractResourceClient {

    /* Properties */
    private String apiPath;

    private Client client;

    public AbstractResourceClient(final Client client, final String apiPath) {
        this.apiPath = apiPath;
        this.client = client;
    }

    /* Properties getters and setters */
    public String getApiPath() {
        return apiPath;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        this.client = client;
    }

    public void setApiPath(final String apiPath) {
        this.apiPath = apiPath;
    }

    protected static void asserValidAuthToken(final String authToken) {
        if (StringUtils.isBlank(authToken)) {
            throw new IllegalArgumentException("Null or empty text was passed as an argument for parameter 'authToken'.");
        }
    }

    protected static Invocation.Builder addAutorizationHeader(final Invocation.Builder requestBuilder, final String authToken) {
        return requestBuilder.header("Authorization", "Bearer " + authToken);
    }
}
