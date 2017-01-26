package com.sfl.nms.services.system.concurrency;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/16/15
 * Time: 12:24 PM
 */
public interface ScheduledTaskExecutorService {

    /**
     * Execute task after provided delay
     *
     * @param runnable
     * @param delay
     * @param timeUnit
     * @param runInPersistenceContext
     *
     */
    void scheduleTaskForExecution(@Nonnull final Runnable runnable, @Nonnull final int delay, @Nonnull final TimeUnit timeUnit, @Nonnull final boolean runInPersistenceContext);
}
