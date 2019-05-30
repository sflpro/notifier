package com.sflpro.notifier.api.model.common.request;


import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/17/15
 * Time: 3:45 PM
 */
@FunctionalInterface
public interface ValidatableRequest {

    /**
     * Validates if required fields are provided
     *
     * @return list of errors
     */
    @Nonnull
    List<ErrorResponseModel> validateRequiredFields();

}
