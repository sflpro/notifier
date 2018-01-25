package com.sflpro.notifier.services.notification.email;

import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationPropertyDto;
import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:16 PM
 */
public interface ThirdPartyEmailNotificationService extends AbstractNotificationService<ThirdPartyEmailNotification> {

    /**
     * Creates new third party email notification
     *
     * @param thirdPartyEmailNotificationDto
     * @param thirdPartyEmailNotificationPropertyDtos
     * @return emailNotification
     */
    @Nonnull
    ThirdPartyEmailNotification createEmailNotification(@Nonnull final ThirdPartyEmailNotificationDto thirdPartyEmailNotificationDto,
                                                        @Nonnull final List<ThirdPartyEmailNotificationPropertyDto> thirdPartyEmailNotificationPropertyDtos);
}
