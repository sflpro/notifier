package com.sflpro.notifier.persistence.utility.impl;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/16/15
 * Time: 6:15 PM
 */
public class SingleTransactionAwarePersistenceUtilityServiceImpl extends PersistenceUtilityServiceImpl {

    /* Constructors */
    public SingleTransactionAwarePersistenceUtilityServiceImpl() {
    }

    @Override
    public void runInNewTransaction(@Nonnull final Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runAfterTransactionCommitIsSuccessful(@Nonnull final Runnable runnable, @Nonnull final boolean executeAsynchronously) {
        runnable.run();
    }
}
