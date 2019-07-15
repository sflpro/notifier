package com.sflpro.notifier.api.client.notification.email.impl;

import com.sflpro.notifier.api.client.notification.email.EmailNotificationResourceClient;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.client.test.AbstractRestApiClientUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class EmailNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private EmailNotificationResourceClient emailNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "notification/email";

    @Before
    public void prepare() {
        emailNotificationResourceClient = new EmailNotificationResourceClientImpl(client, apiPath);
    }

    @Test
    public void testCreateEmailNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> emailNotificationResourceClient.createEmailNotification(null));
        assertThatThrownBy(() -> emailNotificationResourceClient.createEmailNotification(new CreateEmailNotificationRequest(), null));
    }
}
