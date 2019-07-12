package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsIntegrationTest;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/15/15
 * Time: 11:03 AM
 */
@TestPropertySource(properties = {"twillio.enabled=true"})
public class TwillioApiCommunicatorIntegrationTest extends AbstractSmsIntegrationTest {

    /* Constants */
    private static final String VALID_RECIPIENT_PHONE_NUMBER = "+37494668425";

    private static final String INVALID_RECIPIENT_PHONE_NUMBER = "+37494668425";

    private static final String VALID_SENDER_PHONE_NUMBER = "+15005550006";

    private static final String INVALID_SENDER_PHONE_NUMBER = "+37494668425";

    private static final String MESSAGE_BODY = "Happy message body";

    /* Dependencies */
    @Autowired
    private TwillioApiCommunicator twillioApiCommunicator;


    /* Test methods */
    @Test
    public void testSendMessage() {
        /* Test data */
        final SendMessageRequest sendMessageRequest = createValidSendMessageRequest();
        /* Process sending message */
        final SendMessageResponse sendMessageResponse = twillioApiCommunicator.sendMessage(sendMessageRequest);
        /* Assert */
        assertSendMessageRequestResponse(sendMessageRequest, sendMessageResponse);
    }

    @Test
    public void testSendMessageWithInvalidSender() {
        /* Test data */
        final SendMessageRequest sendMessageRequest = createSendMessageRequestWithInvalidSender();
        /* Process sending message */
        try {
            twillioApiCommunicator.sendMessage(sendMessageRequest);
        } catch (final TwillioClientRuntimeException e) {
            assertTwillioClientException(sendMessageRequest, e);
        }
    }

    @Test
    public void testSendMessageWithInvalidRecipient() {
        /* Test data */
        final SendMessageRequest sendMessageRequest = createSendMessageRequestWithInvalidRecipient();
        /* Process sending message */
        try {
            twillioApiCommunicator.sendMessage(sendMessageRequest);
        } catch (final TwillioClientRuntimeException e) {
            assertTwillioClientException(sendMessageRequest, e);
        }
    }

    /* Utility methods */
    private void assertTwillioClientException(final SendMessageRequest sendMessageRequest, final TwillioClientRuntimeException e) {
        assertEquals(e.getRecipientNumber(), sendMessageRequest.getRecipientNumber());
        assertEquals(e.getSenderNumber(), sendMessageRequest.getSenderNumber());
    }

    private void assertSendMessageRequestResponse(final SendMessageRequest request, final SendMessageResponse response) {
        assertNotNull(response);
        assertTrue(StringUtils.isNotEmpty(response.getSid()));
        assertEquals(request.getRecipientNumber(), response.getRecipientNumber());
        assertEquals(request.getMessageBody(), response.getMessageBody());
    }

    private SendMessageRequest createValidSendMessageRequest() {
        return new SendMessageRequest(VALID_SENDER_PHONE_NUMBER, VALID_RECIPIENT_PHONE_NUMBER, MESSAGE_BODY);
    }

    private SendMessageRequest createSendMessageRequestWithInvalidSender() {
        return new SendMessageRequest(INVALID_SENDER_PHONE_NUMBER, VALID_RECIPIENT_PHONE_NUMBER, MESSAGE_BODY);
    }

    private SendMessageRequest createSendMessageRequestWithInvalidRecipient() {
        return new SendMessageRequest(VALID_SENDER_PHONE_NUMBER, INVALID_RECIPIENT_PHONE_NUMBER, MESSAGE_BODY);
    }
}
