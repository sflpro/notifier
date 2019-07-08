package com.sflpro.notifier.api.facade.security;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/27/19
 * Time: 10:31 AM
 */
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(final String message) {
        super(message);
    }
}
