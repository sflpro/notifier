package com.sfl.nms.core.api.internal.model.common.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.nms.core.api.internal.model.common.AbstractResponseModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/17/15
 * Time: 3:27 PM
 */
public class ResultResponseModel<T extends AbstractResponseModel> implements Serializable {

    private static final long serialVersionUID = 6646600561716076786L;

    /* Properties */
    @JsonProperty(value = "errors")
    private List<ErrorResponseModel> errors;

    @JsonProperty(value = "response")
    private T response;

    /* Constructors */
    public ResultResponseModel() {
        this.errors = new ArrayList<>();
    }

    public ResultResponseModel(final T response) {
        this();
        this.response = response;
    }

    public ResultResponseModel(final List<ErrorResponseModel> errors) {
        this.errors = errors;
    }


    /* Getters and setters */
    public List<ErrorResponseModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorResponseModel> errors) {
        this.errors = errors;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T result) {
        this.response = result;
    }

    /* Public methods */
    public boolean checkIfErrorExists(final ErrorType errorType) {
        return hasErrors() && errors.stream().anyMatch(error -> errorType.equals(error.getErrorType()));
    }

    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultResponseModel)) {
            return false;
        }
        final ResultResponseModel that = (ResultResponseModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getErrors(), that.getErrors());
        builder.append(this.getResponse(), that.getResponse());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getErrors());
        builder.append(this.getResponse());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("errors", this.getErrors());
        builder.append("response", this.getResponse());
        return builder.build();
    }
}
