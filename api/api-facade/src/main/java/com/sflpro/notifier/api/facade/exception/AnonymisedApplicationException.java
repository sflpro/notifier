package com.sflpro.notifier.api.facade.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/24/15
 * Time: 9:30 PM
 */
public class AnonymisedApplicationException extends RuntimeException {
    private static final long serialVersionUID = 2862176974634476020L;

    /* Properties  */
    private final String uuId;

    public AnonymisedApplicationException(final String uuId) {
        super("Exception with uuID - " + uuId + " caught during processing the request");
        this.uuId = uuId;
    }

    /* Getters and setters */
    public String getUuId() {
        return uuId;
    }

    /* ToString */
    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("errorUuId", this.getUuId());
        return builder.build();
    }
}
