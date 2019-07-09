package com.sflpro.notifier;

import com.google.common.util.concurrent.MoreExecutors;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import com.sflpro.notifier.services.system.event.impl.ApplicationEventDistributionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/9/19
 * Time: 12:23 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class DirectNotificationProcessorTest {

    private DirectNotificationProcessor sender;

    @Mock
    private NotificationProcessingService notificationProcessingService;

    @Mock
    private PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    private final Executor executor = MoreExecutors.directExecutor();

    private final ApplicationEventDistributionServiceImpl applicationEventDistributionService = new ApplicationEventDistributionServiceImpl();

    @Before
    public void prepare() {
        applicationEventDistributionService.setExecutorService(MoreExecutors.newDirectExecutorService());
        applicationEventDistributionService.setPersistenceUtilityService(persistenceUtilityService);
        sender = new DirectNotificationProcessor(notificationProcessingService, pushNotificationSubscriptionRequestProcessingService, applicationEventDistributionService, executor);
    }

    @Test
    public void testSendingNotification() {
        final StartSendingNotificationEvent event = new StartSendingNotificationEvent(1L, Collections.singletonMap("testKey", "testValue"));
        doAnswer(invocation -> {
            final Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(persistenceUtilityService).runInPersistenceSession(isA(Runnable.class));
        applicationEventDistributionService.publishAsynchronousEvent(event);
        verify(notificationProcessingService).processNotification(event.getNotificationId(), event.getSecureProperties());
    }
}
