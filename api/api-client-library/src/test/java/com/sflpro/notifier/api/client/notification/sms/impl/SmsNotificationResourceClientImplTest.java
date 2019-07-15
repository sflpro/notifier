package com.sflpro.notifier.api.client.notification.sms.impl;

import com.sflpro.notifier.api.client.notification.sms.SmsNotificationResourceClient;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.client.test.AbstractRestApiClientUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SmsNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private SmsNotificationResourceClient smsNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "notification/push";

    @Before
    public void prepare() {
        smsNotificationResourceClient = new SmsNotificationResourceClientImpl(client, apiPath);
    }

    @Test
    public void testCreateSmsNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> smsNotificationResourceClient.createSmsNotification(null));
        assertThatThrownBy(() -> smsNotificationResourceClient.createSmsNotification(new CreateSmsNotificationRequest(), null));
    }
}
