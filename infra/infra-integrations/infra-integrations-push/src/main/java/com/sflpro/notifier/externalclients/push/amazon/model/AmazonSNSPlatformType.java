package com.sflpro.notifier.externalclients.push.amazon.model;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 9/15/15
 * Time: 3:49 PM
 */
public enum AmazonSNSPlatformType {
    GCM("GCM"), APNS("APNS"), APNS_SANDBOX("APNS_SANDBOX");

    private final String jsonKey;

    private AmazonSNSPlatformType(final String jsonKey) {
        this.jsonKey = jsonKey;
    }

    /* Properties getters and setters */
    public String getJsonKey() {
        return jsonKey;
    }
}
