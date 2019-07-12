package com.sflpro.notifier.services.system.concurrency.impl;

import com.sflpro.notifier.services.system.concurrency.ExecutorBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/20/16
 * Time 6:02 PM
 */
@Service("executorBuilder")
public class ExecutorBuilderImpl implements ExecutorBuilder {
    /* Constants */
    private static final int MAX_THREAD_POOL_SIZE = 20;

    private static final int CORE_THREAD_POOL_SIZE = 10;

    /* Constructors */
    public ExecutorBuilderImpl() {
        // Default constructor
    }

    @Override
    public ExecutorService createExecutorService() {
        final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        threadPoolExecutor.setCorePoolSize(CORE_THREAD_POOL_SIZE);
        threadPoolExecutor.setMaximumPoolSize(MAX_THREAD_POOL_SIZE);
        return threadPoolExecutor;
    }
}
