package com.sflpro.notifier.db.entities.user;


import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/30/14
 * Time: 9:01 AM
 */
@Entity
@Table(name = "nms_user")
public class User extends AbstractDomainEntityModel {

    private static final long serialVersionUID = 4537854801373037849L;

    /* Properties */
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuId;

    /* Constructors */
    public User() {
        super();
    }

    /* Properties getters and setters */

    public String getUuId() {
        return uuId;
    }

    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }

    /* Hash code and equals */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(getUuId(), user.getUuId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getUuId());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("uuId", getUuId());
        return builder.build();
    }

}
