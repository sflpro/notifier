package com.sflpro.notifier.services.system.concurrency.impl;

import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.system.concurrency.ScheduledTaskExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/16/15
 * Time: 12:24 PM
 */
@Service
public class ScheduledTaskExecutorServiceImpl implements ScheduledTaskExecutorService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskExecutorServiceImpl.class);

    /* Constants */
    private static final int MAX_THREADS_COUNT = 25;

    /* Dependencies */
    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    private ScheduledExecutorService scheduledExecutorService;

    /* Constructors */
    public ScheduledTaskExecutorServiceImpl() {
        LOGGER.debug("Initializing scheduled task executor service");
    }

    @Override
    public void afterPropertiesSet() {
        // Initializing scheduled task executor service
        initializeScheduledExecutorService();
    }

    @Override
    public void scheduleTaskForExecution(@Nonnull final Runnable runnable, @Nonnull final int delay, @Nonnull final TimeUnit timeUnit, @Nonnull final boolean runInPersistenceContext) {
        Assert.notNull(runnable, "Runnable task should not be null");
        Assert.isTrue(delay > 0, "Delay should be positive integer");
        Assert.notNull(timeUnit, "Time unit should not be null");
        LOGGER.debug("Scheduling task for execution after - {} , time units - {}, run in persistence context - {}", delay, timeUnit, runInPersistenceContext);
        scheduledExecutorService.schedule(new ScheduledTaskExecutorDecorator(runnable, runInPersistenceContext), delay, timeUnit);

    }

    /* Utility methods */
    private void initializeScheduledExecutorService() {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(MAX_THREADS_COUNT);
    }

    /* Properties getters and setters */
    public PersistenceUtilityService getPersistenceUtilityService() {
        return persistenceUtilityService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public void setScheduledExecutorService(final ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /* Inner classes */
    private class ScheduledTaskExecutorDecorator implements Runnable {

        /* Properties */
        private final Runnable runnable;

        private final boolean runInPersistenceContext;

        public ScheduledTaskExecutorDecorator(final Runnable runnable, final boolean runInPersistenceContext) {
            this.runnable = runnable;
            this.runInPersistenceContext = runInPersistenceContext;
        }

        @Override
        public void run() {
            if (runInPersistenceContext) {
                persistenceUtilityService.runInPersistenceSession(runnable);
            } else {
                runnable.run();
            }
        }
    }
}
