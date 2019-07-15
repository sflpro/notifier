package com.sflpro.notifier.api.client.notification.sms.impl;

import com.sflpro.notifier.api.client.notification.sms.SmsNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import com.sflpro.notifier.client.test.AbstractRestApiClientUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;

public class SmsNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private SmsNotificationResourceClient smsNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "http://notification-api/";

    @Before
    public void prepare() {
        smsNotificationResourceClient = new SmsNotificationResourceClientImpl(client, apiPath);
    }

    @Test
    public void testCreateSmsNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> smsNotificationResourceClient.createSmsNotification(null));
        assertThatThrownBy(() -> smsNotificationResourceClient.createSmsNotification(new CreateSmsNotificationRequest(), apiPath));
    }

    @Test
    public void testCreateSmsNotificationWithAuthorization() {
        final CreateSmsNotificationRequest request = new CreateSmsNotificationRequest();
        final String authToken = uuid();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreateSmsNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreateSmsNotificationResponse(new SmsNotificationModel()));
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/sms/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.header("Authorization", "Bearer " + authToken)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(smsNotificationResourceClient.createSmsNotification(request, authToken)).isEqualTo(resultResponseModel);
        verifyAll();
    }

    @Test
    public void testCreateSmsNotification() {
        final CreateSmsNotificationRequest request = new CreateSmsNotificationRequest();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreateSmsNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreateSmsNotificationResponse(new SmsNotificationModel()));
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/sms/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(smsNotificationResourceClient.createSmsNotification(request)).isEqualTo(resultResponseModel);
        verifyAll();
    }
}
