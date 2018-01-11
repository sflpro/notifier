package com.sflpro.notifier.services.system.concurrency.impl;

import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.util.mutable.MutableHolder;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/16/15
 * Time: 12:42 PM
 */
public class ScheduledTaskExecutorServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private ScheduledTaskExecutorServiceImpl scheduledTaskExecutorService = new ScheduledTaskExecutorServiceImpl();

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    /* Constructors */
    public ScheduledTaskExecutorServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testScheduleTaskForExecutionWithInvalidArguments() {
        // Test data
        final Runnable runnable = () -> {
        };
        final int delay = 10;
        final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        final boolean runInPersistenceContext = false;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            scheduledTaskExecutorService.scheduleTaskForExecution(null, delay, timeUnit, runInPersistenceContext);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            scheduledTaskExecutorService.scheduleTaskForExecution(runnable, -1, timeUnit, runInPersistenceContext);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            scheduledTaskExecutorService.scheduleTaskForExecution(runnable, delay, null, runInPersistenceContext);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }


    @Test
    public void testScheduleTaskForExecutionWithPersistenceContext() {
        // Test data
        final MutableHolder<Boolean> booleanMutableHolder = new MutableHolder<>(Boolean.FALSE);
        final Runnable runnable = () -> {
            booleanMutableHolder.setValue(Boolean.TRUE);
        };
        final int delay = 10;
        final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        final boolean runInPersistenceContext = true;
        // Reset
        resetAll();
        // Expectations
        persistenceUtilityService.runInPersistenceSession(eq(runnable));
        expectLastCall().andAnswer(() -> {
            final Runnable taskToExecute = (Runnable) getCurrentArguments()[0];
            taskToExecute.run();
            return null;
        }).once();
        expect(scheduledExecutorService.schedule(isA(Runnable.class), eq(Long.valueOf(delay).longValue()), eq(timeUnit))).andAnswer(() -> {
            final Runnable taskToExecute = (Runnable) getCurrentArguments()[0];
            taskToExecute.run();
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        scheduledTaskExecutorService.scheduleTaskForExecution(runnable, delay, timeUnit, runInPersistenceContext);
        assertTrue(booleanMutableHolder.getValue().booleanValue());
        // Verify
        verifyAll();
    }

    @Test
    public void testScheduleTaskForExecutionWithoutPersistenceContext() {
        // Test data
        final MutableHolder<Boolean> booleanMutableHolder = new MutableHolder<>(Boolean.FALSE);
        final Runnable runnable = () -> {
            booleanMutableHolder.setValue(Boolean.TRUE);
        };
        final int delay = 10;
        final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        final boolean runInPersistenceContext = false;
        // Reset
        resetAll();
        // Expectations
        expect(scheduledExecutorService.schedule(isA(Runnable.class), eq(Long.valueOf(delay).longValue()), eq(timeUnit))).andAnswer(() -> {
            final Runnable taskToExecute = (Runnable) getCurrentArguments()[0];
            taskToExecute.run();
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        scheduledTaskExecutorService.scheduleTaskForExecution(runnable, delay, timeUnit, runInPersistenceContext);
        assertTrue(booleanMutableHolder.getValue().booleanValue());
        // Verify
        verifyAll();
    }
}
