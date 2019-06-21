package com.sflpro.notifier.services.system.concurrency.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/8/14
 * Time: 12:36 AM
 */
public class SynchronExecutorService implements ExecutorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronExecutorService.class);

    /* Properties */
    private boolean shutDown;

    /* Constructors */
    public SynchronExecutorService() {
        super();
    }

    @Override
    public void execute(Runnable command) {
        // execute
    }


    @Override
    public void shutdown() {
        LOGGER.debug("Shutting down executor service");
        shutDown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        LOGGER.debug("Shutting down executor service");
        shutDown = true;
        return new ArrayList<>();
    }

    @Override
    public boolean isShutdown() {
        return shutDown;
    }

    @Override
    public boolean isTerminated() {
        return shutDown;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return true;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        final T result = invokeCallable(task);
        return new SynchronFuture<>(result);
    }

    private <T> T invokeCallable(Callable<T> task) {
        T result = null;
        try {
            LOGGER.debug("Executing submitted task");
            result = task.call();
        } catch (Exception ex) {
            LOGGER.error("Error occured during task execution", ex);
        }
        return result;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        LOGGER.debug("Executing submitted task");
        task.run();
        return new SynchronFuture<>(result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        LOGGER.debug("Executing submitted task");
        task.run();
        return new SynchronFuture<>(null);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        final List<Future<T>> futures = new ArrayList<>();
        for (final Callable<T> task : tasks) {
            futures.add(submit(task));
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return invokeCallable(tasks.iterator().next());
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(tasks);
    }

    public static ExecutorService createExecutorService() {
        LOGGER.debug("Creating synchron executor service");
        return new SynchronExecutorService();
    }

}
