package com.sflpro.notifier.services.notification.push.sns;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientServiceIntegrationTest;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:38 PM
 */
public class PushNotificationSnsRecipientServiceIntegrationTest extends AbstractPushNotificationRecipientServiceIntegrationTest<PushNotificationRecipient, PushNotificationRecipientDto> {

    /* Dependencies */
    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    /* Constructors */
    public PushNotificationSnsRecipientServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testCreatePushNotificationRecipient() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipientDto recipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        flushAndClear();
        // Create push notification recipient
        PushNotificationRecipient recipient = pushNotificationRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
        assertPushNotificationSnsRecipient(recipient, recipientDto, subscription);
        // Flush, clear, reload and assert again
        flushAndClear();
        recipient = pushNotificationRecipientService.getPushNotificationRecipientById(recipient.getId());
        assertPushNotificationSnsRecipient(recipient, recipientDto, subscription);
    }

    @Test
    public void testCreatePushNotificationRecipientWhenItAlreadyExists() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipientDto recipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        flushAndClear();
        // Create push notification recipient
        PushNotificationRecipient recipient = pushNotificationRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
        assertNotNull(recipient);
        try {
            pushNotificationRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientAlreadyExistsException ex) {
            // Expected
        }
        // Flush, clear, reload and assert again
        flushAndClear();
        try {
            pushNotificationRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientAlreadyExistsException ex) {
            // Expected
        }
    }

    /* Utility methods */
    private void assertPushNotificationSnsRecipient(final PushNotificationRecipient recipient, final PushNotificationRecipientDto recipientDto, final PushNotificationSubscription subscription) {
        getServicesTestHelper().assertPushNotificationSnsRecipient(recipient, recipientDto);
        assertNotNull(recipient.getSubscription());
        Assert.assertEquals(subscription.getId(), recipient.getSubscription().getId());
    }

    @Override
    protected AbstractPushNotificationRecipientService<PushNotificationRecipient> getService() {
        return pushNotificationRecipientService;
    }

    @Override
    protected PushNotificationRecipient getInstance() {
        return getServicesTestHelper().createPushNotificationSnsRecipient();
    }

    @Override
    protected PushNotificationRecipient getInstance(final User user) {
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription(user, getServicesTestHelper().createPushNotificationSubscriptionDto());
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, getServicesTestHelper().createPushNotificationSnsRecipientDto());
    }

    @Override
    protected PushNotificationRecipient getInstance(final PushNotificationSubscription subscription, final PushNotificationRecipientDto recipientDto) {
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, recipientDto);
    }

    @Override
    protected PushNotificationRecipientDto getInstanceDto() {
        return getServicesTestHelper().createPushNotificationSnsRecipientDto();
    }

    @Override
    protected Class<PushNotificationRecipient> getInstanceClass() {
        return PushNotificationRecipient.class;
    }


}
