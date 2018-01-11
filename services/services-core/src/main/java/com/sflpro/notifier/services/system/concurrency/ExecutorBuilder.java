package com.sflpro.notifier.services.system.concurrency;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/20/16
 * Time 6:10 PM
 */
@FunctionalInterface
public interface ExecutorBuilder {

    /**
     * Creates executor service
     *
     * @return Executor service
     */
    @Nonnull
    ExecutorService createExecutorService();
}
