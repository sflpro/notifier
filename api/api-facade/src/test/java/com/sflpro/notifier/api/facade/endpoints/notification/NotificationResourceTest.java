package com.sflpro.notifier.api.facade.endpoints.notification;

import com.sflpro.notifier.api.facade.services.NotificationFacade;
import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotificationResourceTest {

    @Mock
    private NotificationFacade notificationFacade;

    @InjectMocks
    private NotificationResource notificationResource;

    @Test
    public void get() {
        final Long id = RandomUtils.nextLong(1, 100);
        final EmailNotificationModel emailNotification = new EmailNotificationModel();
        emailNotification.setId(id);
        Mockito.when(notificationFacade.get(id)).thenReturn(emailNotification);

        final NotificationModel notificationModel = notificationResource.get(id);
        Assert.assertEquals(id, notificationModel.getId());
    }
}