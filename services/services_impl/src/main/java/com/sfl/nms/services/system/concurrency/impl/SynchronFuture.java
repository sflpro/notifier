package com.sfl.nms.services.system.concurrency.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/8/14
 * Time: 12:38 AM
 */
public class SynchronFuture<T> implements Future<T> {

    /* Properties */
    private T result;

    private boolean canceled;

    /* Constructors */
    public SynchronFuture(T result) {
        this.result = result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        canceled = true;
        return false;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return result;
    }

}
