package com.sflpro.notifier.api.client.notification.push.impl;

import com.sflpro.notifier.api.client.notification.push.PushNotificationResourceClient;
import com.sflpro.notifier.api.client.notification.sms.impl.SmsNotificationResourceClientImpl;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.client.test.AbstractRestApiClientUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PushNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private PushNotificationResourceClient pushNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "notification/sms";

    @Before
    public void prepare() {
        pushNotificationResourceClient = new PushNotificationResourceClientImpl(client, apiPath);
    }

    @Test
    public void testCreateSmsNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> pushNotificationResourceClient.createPushNotification(null));
        assertThatThrownBy(() -> pushNotificationResourceClient.createPushNotification(new CreatePushNotificationRequest(), null));
    }
}
