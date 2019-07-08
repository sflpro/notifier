package com.sflpro.notifier.externalclients.push.amazon;

import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationUnitTest;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 3:44 PM
 */
public class AmazonSnsPushMessageSubscriberTest extends AbstractPushNotificationUnitTest {

    private PushMessageSubscriber messageSubscriber;

    @Mock
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Before
    public void prepare() {
        messageSubscriber = new AmazonSnsPushMessageSubscriber(
                amazonSnsApiCommunicator
        );
    }

    @Test
    public void testRefreshDeviceEndpointArnWithInvalidArguments() {
        Assertions.assertThatThrownBy(() -> messageSubscriber.refreshDeviceEndpointArn(null, uuid(), uuid()))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> messageSubscriber.refreshDeviceEndpointArn(uuid(), null, uuid()))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> messageSubscriber.refreshDeviceEndpointArn(uuid(), uuid(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testRefreshDeviceEndpointArnExistingTokenValid() {
        final String existingDeviceEndpointArn = uuid();
        final String userDeviceToken = uuid();
        final String applicationArn = uuid();
        final GetDeviceEndpointAttributesRequest request = new GetDeviceEndpointAttributesRequest(existingDeviceEndpointArn);
        final GetDeviceEndpointAttributesResponse response = new GetDeviceEndpointAttributesResponse(existingDeviceEndpointArn,userDeviceToken,true,true);
        when(amazonSnsApiCommunicator.getDeviceEndpointAttributes(request)).thenReturn(response);
        assertThat( messageSubscriber.refreshDeviceEndpointArn(existingDeviceEndpointArn, userDeviceToken, applicationArn)).isEqualTo(existingDeviceEndpointArn);
        verify(amazonSnsApiCommunicator).getDeviceEndpointAttributes(request);
    }

    @Test
    public void testRgisterDeviceEndpointArnWithInvalidArguments() {
        Assertions.assertThatThrownBy(() -> messageSubscriber.registerDeviceEndpointArn(null, uuid()))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> messageSubscriber.registerDeviceEndpointArn(uuid(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
