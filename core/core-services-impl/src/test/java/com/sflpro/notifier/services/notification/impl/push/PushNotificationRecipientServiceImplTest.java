package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientSearchFilter;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientSearchParameters;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:28 PM
 */
public class PushNotificationRecipientServiceImplTest extends AbstractPushNotificationRecipientServiceImplTest<PushNotificationRecipient> {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationRecipientServiceImpl pushNotificationRecipientService = new PushNotificationRecipientServiceImpl();

    /* Constructors */
    public PushNotificationRecipientServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParameters() {
        // Test data
        final PushNotificationRecipientSearchParameters parameters = createPushNotificationRecipientSearchParameters();
        final PushNotificationRecipientSearchFilter filters = createPushNotificationRecipientSearchFilters(parameters);
        final Long recipientsCount = 10L;
        // Reset
        resetAll();
        // Expectations
        expect(getPushNotificationRecipientRepository().getPushNotificationRecipientsCount(eq(filters))).andReturn(recipientsCount).once();
        // Replay
        replayAll();
        // Run test scenario
        final Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertEquals(recipientsCount, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithInvalidArguments() {
        // Test data
        final PushNotificationRecipientSearchParameters parameters = createPushNotificationRecipientSearchParameters();
        final Long startFrom = 0l;
        final Integer maxResults = 10;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(null, startFrom, maxResults);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, null, maxResults);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, startFrom, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, -1L, maxResults);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, startFrom, 0);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParameters() {
        // Test data
        final PushNotificationRecipientSearchParameters parameters = createPushNotificationRecipientSearchParameters();
        final PushNotificationRecipientSearchFilter filter = createPushNotificationRecipientSearchFilters(parameters);
        final Long startFrom = 0l;
        final Integer maxResults = 10;
        final List<PushNotificationRecipient> recipients = createRecipients(10);
        // Reset
        resetAll();
        // Expectations
        expect(getPushNotificationRecipientRepository().findPushNotificationRecipients(eq(filter), eq(startFrom.longValue()), eq(maxResults.intValue()))).andReturn(recipients).once();
        // Replay
        replayAll();
        // Run test scenario
        final List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, startFrom, maxResults);
        assertEquals(recipients, result);
        // Verify
        verifyAll();
    }

    /* Utility methods */
    @Override
    protected PushNotificationRecipient getInstance() {
        return getServicesImplTestHelper().createPushNotificationSnsRecipient();
    }

    @Override
    protected Class<PushNotificationRecipient> getInstanceClass() {
        return PushNotificationRecipient.class;
    }

    @Override
    protected AbstractPushNotificationRecipientServiceImpl<PushNotificationRecipient> getService() {
        return pushNotificationRecipientService;
    }

    @Override
    protected AbstractPushNotificationRecipientRepository<PushNotificationRecipient> getRepository() {
        return getPushNotificationRecipientRepository();
    }

    private PushNotificationRecipientSearchParameters createPushNotificationRecipientSearchParameters() {
        final PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        parameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        parameters.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        parameters.setDestinationRouteToken("GGFTF%*^D(RD*RDFXR58drS&D*");
        parameters.setProviderType(PushNotificationProviderType.SNS);
        parameters.setSubscriptionId(1L);
        return parameters;
    }

    private PushNotificationRecipientSearchFilter createPushNotificationRecipientSearchFilters(PushNotificationRecipientSearchParameters parameters) {
        final PushNotificationRecipientSearchFilter filter = new PushNotificationRecipientSearchFilter();
        filter.setStatus(parameters.getStatus());
        filter.setDeviceOperatingSystemType(parameters.getDeviceOperatingSystemType());
        filter.setDestinationRouteToken(parameters.getApplicationType());
        filter.setProviderType(parameters.getProviderType());
        filter.setSubscriptionId(parameters.getSubscriptionId());
        return filter;
    }

    private List<PushNotificationRecipient> createRecipients(final int count) {
        final List<PushNotificationRecipient> recipients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
            recipient.setId(Long.valueOf(i));
            recipients.add(recipient);
        }
        return recipients;
    }
}
