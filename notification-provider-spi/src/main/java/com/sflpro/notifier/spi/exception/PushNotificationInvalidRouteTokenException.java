package com.sflpro.notifier.spi.exception;

public class PushNotificationInvalidRouteTokenException extends RuntimeException {

    private final String routeToken;

    public PushNotificationInvalidRouteTokenException(final String routeToken, final String message, final Throwable cause) {
        super(message, cause);
        this.routeToken = routeToken;
    }

    public String getRouteToken() {
        return routeToken;
    }
}
