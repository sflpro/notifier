package com.sflpro.notifier.api.client.notification.push.impl;

import com.sflpro.notifier.api.client.notification.push.PushNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
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

public class PushNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private PushNotificationResourceClient pushNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "http://notification-api/";

    @Before
    public void prepare() {
        pushNotificationResourceClient = new PushNotificationResourceClientImpl(client, apiPath);
    }

    //region createPushNotification...

    @Test
    public void testCreatePushNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> pushNotificationResourceClient.createPushNotification((CreatePushNotificationRequest) null));
        assertThatThrownBy(() -> pushNotificationResourceClient.createPushNotification(new CreatePushNotificationRequest(), null));
    }

    @Test
    public void testCreatePushNotificationWithAuthorization() {
        final CreatePushNotificationRequest request = new CreatePushNotificationRequest();
        final String authToken = uuid();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreatePushNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreatePushNotificationResponse());
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/push/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.header("Authorization", "Bearer " + authToken)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(pushNotificationResourceClient.createPushNotification(request, authToken)).isEqualTo(resultResponseModel);
        verifyAll();
    }

    @Test
    public void testCreatePushNotification() {
        final CreatePushNotificationRequest request = new CreatePushNotificationRequest();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreatePushNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreatePushNotificationResponse());
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/push/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(pushNotificationResourceClient.createPushNotification(request)).isEqualTo(resultResponseModel);
        verifyAll();
    }

    //endregion

}
