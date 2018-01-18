package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientSearchFilter;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:38 PM
 */
public class PushNotificationRecipientServiceIntegrationTest extends AbstractPushNotificationRecipientServiceIntegrationTest<PushNotificationRecipient, PushNotificationSnsRecipientDto> {

    /* Dependencies */
    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    /* Constructors */
    public PushNotificationRecipientServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithProviderType() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(PushNotificationProviderType.SNS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(PushNotificationProviderType.APNS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>());
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithProviderType() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(PushNotificationProviderType.SNS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(PushNotificationProviderType.APNS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>());
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithApplicationType() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        firstSnsRecipientDto.setApplicationType("customer");
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        secondSnsRecipientDto.setApplicationType("operator");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setApplicationType("customer");
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setApplicationType("operator");
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithOperatingSystem() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        firstSnsRecipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        secondSnsRecipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.ANDROID);
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDeviceOperatingSystemType(DeviceOperatingSystemType.ANDROID);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithOperatingSystem() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        firstSnsRecipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        secondSnsRecipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.ANDROID);
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDeviceOperatingSystemType(DeviceOperatingSystemType.ANDROID);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithStatus() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        assertEquals(PushNotificationRecipientStatus.ENABLED, firstRecipient.getStatus());
        PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        secondRecipient = pushNotificationRecipientService.updatePushNotificationRecipientStatus(secondRecipient.getId(), PushNotificationRecipientStatus.DISABLED);
        assertEquals(PushNotificationRecipientStatus.DISABLED, secondRecipient.getStatus());
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setStatus(PushNotificationRecipientStatus.DISABLED);
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithStatus() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        assertEquals(PushNotificationRecipientStatus.ENABLED, firstRecipient.getStatus());
        PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        secondRecipient = pushNotificationRecipientService.updatePushNotificationRecipientStatus(secondRecipient.getId(), PushNotificationRecipientStatus.DISABLED);
        assertEquals(PushNotificationRecipientStatus.DISABLED, secondRecipient.getStatus());
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setStatus(PushNotificationRecipientStatus.DISABLED);
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithSubscription() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final User firstCustomer = getServicesTestHelper().createUser();
        final User secondCustomer = getServicesTestHelper().createUser();
        final PushNotificationSubscription firstSubscription = getServicesTestHelper().createPushNotificationSubscription(firstCustomer, getServicesTestHelper().createPushNotificationSubscriptionDto());
        final PushNotificationSubscription secondSubscription = getServicesTestHelper().createPushNotificationSubscription(secondCustomer, getServicesTestHelper().createPushNotificationSubscriptionDto());
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(firstSubscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(secondSubscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setSubscriptionId(firstSubscription.getId());
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setSubscriptionId(secondSubscription.getId());
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithSubscription() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final User firstCustomer = getServicesTestHelper().createUser();
        final User secondCustomer = getServicesTestHelper().createUser();
        final PushNotificationSubscription firstSubscription = getServicesTestHelper().createPushNotificationSubscription(firstCustomer, getServicesTestHelper().createPushNotificationSubscriptionDto());
        final PushNotificationSubscription secondSubscription = getServicesTestHelper().createPushNotificationSubscription(secondCustomer, getServicesTestHelper().createPushNotificationSubscriptionDto());
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(firstSubscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(secondSubscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setSubscriptionId(firstSubscription.getId());
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setSubscriptionId(secondSubscription.getId());
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsForSearchParametersWithDestinationRouteToken() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        List<PushNotificationRecipient> result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDestinationRouteToken(firstRecipient.getDestinationRouteToken());
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDestinationRouteToken(secondRecipient.getDestinationRouteToken());
        result = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, 0L, Integer.MAX_VALUE);
        assertPushNotificationRecipients(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    @Test
    public void testGetPushNotificationRecipientsCountForSearchParametersWithDestinationRouteToken() {
        // Prepare data
        final PushNotificationSnsRecipientDto firstSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        final PushNotificationSnsRecipientDto secondSnsRecipientDto = getServicesTestHelper().createPushNotificationSnsRecipientDto();
        secondSnsRecipientDto.setDestinationRouteToken(firstSnsRecipientDto.getDestinationRouteToken() + "_second");
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient firstRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, firstSnsRecipientDto);
        final PushNotificationRecipient secondRecipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, secondSnsRecipientDto);
        flushAndClear();
        // Load recipients and assert
        PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        // Execute search with all recipients
        Long result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId(), secondRecipient.getId())));
        // Execute search with first recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDestinationRouteToken(firstRecipient.getDestinationRouteToken());
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(firstRecipient.getId())));
        // Execute search with second recipient
        parameters = new PushNotificationRecipientSearchParameters();
        parameters.setDestinationRouteToken(secondRecipient.getDestinationRouteToken());
        result = pushNotificationRecipientService.getPushNotificationRecipientsCountForSearchParameters(parameters);
        assertPushNotificationRecipientsCount(result, new LinkedHashSet<>(Arrays.asList(secondRecipient.getId())));
    }

    /* Utility methods */
    private void assertPushNotificationRecipients(final List<PushNotificationRecipient> result, final Set<Long> expectedRecipientIds) {
        assertEquals(expectedRecipientIds.size(), result.size());
        result.forEach(recipient -> {
            assertTrue(expectedRecipientIds.contains(recipient.getId()));
        });
    }

    private void assertPushNotificationRecipientsCount(final Long result, final Set<Long> expectedRecipientIds) {
        assertEquals(expectedRecipientIds.size(), result.intValue());
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
    protected PushNotificationRecipient getInstance(final PushNotificationSubscription subscription, final PushNotificationSnsRecipientDto recipientDto) {
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, recipientDto);
    }

    @Override
    protected PushNotificationSnsRecipientDto getInstanceDto() {
        return getServicesTestHelper().createPushNotificationSnsRecipientDto();
    }

    @Override
    protected PushNotificationRecipient getInstance(final User user) {
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription(user, getServicesTestHelper().createPushNotificationSubscriptionDto());
        return getServicesTestHelper().createPushNotificationSnsRecipient(subscription, getServicesTestHelper().createPushNotificationSnsRecipientDto());
    }

    @Override
    protected Class<PushNotificationRecipient> getInstanceClass() {
        return PushNotificationRecipient.class;
    }


}
