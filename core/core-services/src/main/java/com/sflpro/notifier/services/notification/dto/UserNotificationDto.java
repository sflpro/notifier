package com.sflpro.notifier.services.notification.dto;


import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:36 PM
 */
public class UserNotificationDto extends AbstractDomainEntityModelDto<UserNotification> {
    private static final long serialVersionUID = -6480459757050740097L;

    /* Properties */

    /* Constructors */
    public UserNotificationDto() {
        super();
    }

    /* Properties getters and setters */

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final UserNotification userNotification) {
        Assert.notNull(userNotification, "User notification should not be null");
    }

}
