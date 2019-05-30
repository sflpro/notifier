package com.sflpro.notifier.api.model.common.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/17/15
 * Time: 3:27 PM
 */
public class ErrorResponseModel implements Serializable {

    private static final long serialVersionUID = -8457328784601461833L;

    /* Properties */
    @JsonProperty("errorType")
    private final ErrorType errorType;

    public ErrorResponseModel(final ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
