package com.sflpro.notifier.services.user.dto;

import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import com.sflpro.notifier.services.user.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/9/16
 * Time: 7:08 PM
 */
public class UserDto extends AbstractDomainEntityModelDto<User> {

    /* Properties */
    private String uuId;

    /* Constructors */
    public UserDto() {
    }

    public UserDto(final String uuId) {
        this.uuId = uuId;
    }

    /* Properties getters and setters */
    public String getUuId() {
        return uuId;
    }

    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }

    /* Utility methods */
    @Override
    public void updateDomainEntityProperties(final User user) {
        Assert.notNull(user, "User should not be null");
        user.setUuId(uuId);
    }

    /* Hash code and equals */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDto)) {
            return false;
        }
        final UserDto user = (UserDto) o;
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
