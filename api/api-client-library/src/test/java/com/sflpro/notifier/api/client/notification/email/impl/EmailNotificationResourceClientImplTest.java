package com.sflpro.notifier.api.client.notification.email.impl;

import com.sflpro.notifier.api.client.notification.email.EmailNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
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


public class EmailNotificationResourceClientImplTest extends AbstractRestApiClientUnitTest {

    private EmailNotificationResourceClient emailNotificationResourceClient;

    @Mock
    private javax.ws.rs.client.Client client;

    private final String apiPath = "http://notification-api/";

    @Before
    public void prepare() {
        emailNotificationResourceClient = new EmailNotificationResourceClientImpl(client, apiPath);
    }

    @Test
    public void testCreateEmailNotificationWithInvalidArgument() {
        assertThatThrownBy(() -> emailNotificationResourceClient.createEmailNotification(null));
        assertThatThrownBy(() -> emailNotificationResourceClient.createEmailNotification(new CreateEmailNotificationRequest(), null));
    }

    @Test
    public void testCreateEmailNotificationWithAutorization() {
        final CreateEmailNotificationRequest request = new CreateEmailNotificationRequest();
        final String authToken = uuid();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreateEmailNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreateEmailNotificationResponse());
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/email/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.header("Authorization", "Bearer " + authToken)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(emailNotificationResourceClient.createEmailNotification(request, authToken)).isEqualTo(resultResponseModel);
        verifyAll();
    }

    @Test
    public void testCreateEmailNotification() {
        final CreateEmailNotificationRequest request = new CreateEmailNotificationRequest();
        final WebTarget webTarget = createMock(WebTarget.class);
        final Invocation.Builder requestBuilder = createMock(Invocation.Builder.class);
        final ResultResponseModel<CreateEmailNotificationResponse> resultResponseModel = new ResultResponseModel<>(new CreateEmailNotificationResponse());
        expect(client.target(apiPath)).andReturn(webTarget);
        expect(webTarget.path("notification/email/create")).andReturn(webTarget);
        expect(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).andReturn(requestBuilder);
        expect(requestBuilder.post(eq(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE)), isA(GenericType.class))).andReturn(resultResponseModel);
        replayAll();
        assertThat(emailNotificationResourceClient.createEmailNotification(request)).isEqualTo(resultResponseModel);
        verifyAll();
    }
}
