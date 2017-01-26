package com.sfl.nms.services.common.exception;

import com.sfl.nms.services.common.model.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/15/14
 * Time: 10:41 AM
 */
public class EntityNotFoundForIdException extends ServicesRuntimeException {

    private static final long serialVersionUID = -5263374036253353964L;

    /* Properties */
    private final Long id;

    private final Class<? extends AbstractDomainEntityModel> entityClass;

    public EntityNotFoundForIdException(final Long id, final Class<? extends AbstractDomainEntityModel> entityClass) {
        super("Entity with id " + id + " of class - " + entityClass.getName() + " is not found");
        this.id = id;
        this.entityClass = entityClass;
    }

    /* Getters and setters */
    public Long getId() {
        return id;
    }

    public Class<? extends AbstractDomainEntityModel> getEntityClass() {
        return entityClass;
    }

    /* ToString */

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("id", getId());
        builder.append("entityClass", getEntityClass());
        return builder.build();
    }
}
