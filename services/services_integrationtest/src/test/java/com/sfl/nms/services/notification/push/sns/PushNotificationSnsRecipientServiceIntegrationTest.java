package com.sfl.nms.services.notification.push.sns;

import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.services.notification.push.AbstractPushNotificationRecipientService;
import com.sfl.nms.services.notification.push.AbstractPushNotificationRecipientServiceIntegrationTest;
import com.sfl.nms.services.user.model.User;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:38 PM
 */
public class PushNotificationSnsRecipientServiceIntegrationTest extends AbstractPushNotificationRecipientServiceIntegrationTest<PushNotificationSnsRecipient, PushNotificationSnsRecipientDto> {

    /* Dependencies */
    @Autowired
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    /* Constructors */
    public PushNotificationSnsRecipientServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testCreatePushNotificationRecipient() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationSnsRecipientDto recipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        flushAndClear();
        // Create push notification recipient
        PushNotificationSnsRecipient recipient = pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
        assertPushNotificationSnsRecipient(recipient, recipientDto, subscription);
        // Flush, clear, reload and assert again
        flushAndClear();
        recipient = pushNotificationSnsRecipientService.getPushNotificationRecipientById(recipient.getId());
        assertPushNotificationSnsRecipient(recipient, recipientDto, subscription);
    }

    @Test
    public void testCreatePushNotificationRecipientWhenItAlreadyExists() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationSnsRecipientDto recipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        flushAndClear();
        // Create push notification recipient
        PushNotificationSnsRecipient recipient = pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
        assertNotNull(recipient);
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientAlreadyExistsException ex) {
            // Expected
        }
        // Flush, clear, reload and assert again
        flushAndClear();
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientAlreadyExistsException ex) {
            // Expected
        }
    }

    /* Utility methods */
    private void assertPushNotificationSnsRecipient(final PushNotificationSnsRecipient recipient, final PushNotificationSnsRecipientDto recipientDto, final PushNotificationSubscription subscription) {
        getServicesTestHelper().assertPushNotificationSnsRecipient(recipient, recipientDto);
        assertNotNull(recipient.getSubscription());
        assertEquals(subscription.getId(), recipient.getSubscription().getId());
    }

    @Override
    protected AbstractPushNotificationRecipientService<PushNotificationSnsRecipient> getService() {
        return pushNotificationSnsRecipientService;
    }

    @Override
    protected PushNotificationSnsRecipient getInstance() {
        return getServicesTestHelper().createPushNotificationSnsRecipient();
    }

    @Override
    protected PushNotificationSnsRecipient getInstance(final User user) {
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription(user, getServicesTestHelper().createPushNotificationSubscriptionDto());
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, getServicesTestHelper().createPushNotificationSnsRecipientDto());
    }

    @Override
    protected PushNotificationSnsRecipient getInstance(final PushNotificationSubscription subscription, final PushNotificationSnsRecipientDto recipientDto) {
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, recipientDto);
    }

    @Override
    protected PushNotificationSnsRecipientDto getInstanceDto() {
        return getServicesTestHelper().createPushNotificationSnsRecipientDto();
    }

    @Override
    protected Class<PushNotificationSnsRecipient> getInstanceClass() {
        return PushNotificationSnsRecipient.class;
    }


}
