package com.sflpro.notifier.services.notification.push.sns;

import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientServiceIntegrationTest;
import com.sflpro.notifier.services.user.model.User;
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
        Assert.assertEquals(subscription.getId(), recipient.getSubscription().getId());
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
