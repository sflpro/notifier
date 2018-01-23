package com.sflpro.notifier.services.system.event.impl;

import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.system.event.model.ApplicationEvent;
import com.sflpro.notifier.services.system.event.model.ApplicationEventListener;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 10:41 PM
 */
public class ApplicationEventDistributionServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private ApplicationEventDistributionServiceImpl applicationEventDistributionService = new ApplicationEventDistributionServiceImpl();

    @Mock
    private ApplicationEventListener applicationEventListener;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private ExecutorService executorService;

    /* Constructors */
    public ApplicationEventDistributionServiceImplTest() {
    }

    /* Test methods */

    @Test
    public void testPublishSynchronousEventWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            applicationEventDistributionService.publishSynchronousEvent(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testPublishSynchronousEvent() {
        // Test data
        final Long stationId = 1l;
        final ApplicationEvent applicationEvent = new MockApplicationEvent();
        // Reset
        resetAll();
        // Expectation
        expect(applicationEventListener.subscribed(eq(applicationEvent))).andReturn(true).once();
        applicationEventListener.process(eq(applicationEvent));
        expectLastCall().once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        applicationEventDistributionService.subscribe(applicationEventListener);
        applicationEventDistributionService.publishSynchronousEvent(applicationEvent);
        // Verify
        verifyAll();
    }

    @Test
    public void testSubscribeWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            applicationEventDistributionService.subscribe(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testSubscribe() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        applicationEventDistributionService.subscribe(new ApplicationEventListener() {
            @Nonnull
            @Override
            public boolean subscribed(@Nonnull ApplicationEvent applicationEvent) {
                return false;
            }

            @Override
            public void process(@Nonnull ApplicationEvent applicationEvent) {

            }
        });
        // Verify
        verifyAll();
    }

    @Test
    public void testPublishAsynchronousEventWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            applicationEventDistributionService.publishAsynchronousEvent(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testPublishAsynchronousEvent() {
        // Test data
        final Long stationId = 1l;
        final ApplicationEvent applicationEvent = new MockApplicationEvent();
        // Reset
        resetAll();
        // Expectation
        expect(executorService.submit(isA(Runnable.class))).andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).times(2);
        expect(applicationEventListener.subscribed(eq(applicationEvent))).andReturn(true).once();
        applicationEventListener.process(eq(applicationEvent));
        expectLastCall().once();
        persistenceUtilityService.runInPersistenceSession(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        applicationEventDistributionService.subscribe(applicationEventListener);
        applicationEventDistributionService.publishAsynchronousEvent(applicationEvent);
        // Verify
        verifyAll();
    }

    /* Inner class */
    private static class MockApplicationEvent implements ApplicationEvent {

        /* Constructors */
        public MockApplicationEvent() {
        }

    }
}
