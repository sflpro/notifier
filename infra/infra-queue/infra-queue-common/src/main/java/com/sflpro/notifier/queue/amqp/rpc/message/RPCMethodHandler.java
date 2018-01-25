package com.sflpro.notifier.queue.amqp.rpc.message;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:40 PM
 */
public abstract class RPCMethodHandler<T> {

    /* Properties */
    private final String methodIdentifier;

    private final Class<T> methodParameterClass;

    /* Constructors */
    public RPCMethodHandler(final String methodIdentifier, final Class<T> methodParameterClass) {
        Assert.notNull(methodIdentifier, "methodIdentifier cannot be null");
        Assert.notNull(methodParameterClass, "methodParameterClass cannot be null");
        this.methodIdentifier = methodIdentifier;
        this.methodParameterClass = methodParameterClass;
    }


    /* Properties getters and setters */
    public String getMethodIdentifier() {
        return methodIdentifier;
    }

    public Class<T> getMethodParameterClass() {
        return methodParameterClass;
    }


    /* Abstract methods */
    public abstract Object executeMethod(final Object parameter);

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RPCMethodHandler)) {
            return false;
        }
        final RPCMethodHandler that = (RPCMethodHandler) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getMethodIdentifier(), that.getMethodIdentifier());
        builder.append(this.getMethodParameterClass(), that.getMethodParameterClass());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getMethodIdentifier());
        builder.append(this.getMethodParameterClass());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("methodIdentifier", this.getMethodIdentifier());
        builder.append("methodParameterClass", this.getMethodParameterClass());
        return builder.build();
    }

}