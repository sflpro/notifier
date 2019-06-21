package com.sflpro.notifier.api.client.common;

import javax.ws.rs.client.Client;

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

    /* Constructors */
    public AbstractResourceClient() {
        // Default constructor
    }

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
}
