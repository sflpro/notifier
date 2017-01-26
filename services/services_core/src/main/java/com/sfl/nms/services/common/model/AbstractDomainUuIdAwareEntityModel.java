package com.sfl.nms.services.common.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/27/15
 * Time: 4:42 PM
 */
@MappedSuperclass
public abstract class AbstractDomainUuIdAwareEntityModel extends AbstractDomainEntityModel {
    private static final long serialVersionUID = 3899417789871880941L;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuId;

    /* Constructors */
    public AbstractDomainUuIdAwareEntityModel() {
    }

    public AbstractDomainUuIdAwareEntityModel(final boolean generateUuId) {
        if (generateUuId) {
            this.uuId = UUID.randomUUID().toString();
        }
    }

    /* Properties getters and setters */
    public String getUuId() {
        return uuId;
    }

    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDomainUuIdAwareEntityModel)) {
            return false;
        }
        final AbstractDomainUuIdAwareEntityModel that = (AbstractDomainUuIdAwareEntityModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getUuId(), that.getUuId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUuId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("uuId", this.getUuId());
        return builder.build();
    }
}
