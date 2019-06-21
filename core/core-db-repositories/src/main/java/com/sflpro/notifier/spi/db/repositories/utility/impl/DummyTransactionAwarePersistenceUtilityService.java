package com.sflpro.notifier.db.repositories.utility.impl;

import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/16/15
 * Time: 6:15 PM
 */
class DummyTransactionAwarePersistenceUtilityService implements PersistenceUtilityService {



    /* Constructors */
    public DummyTransactionAwarePersistenceUtilityService() {
        super();
    }

    @Override
    public void runInNewTransaction(@Nonnull final Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runAfterTransactionCommitIsSuccessful(@Nonnull final Runnable runnable, @Nonnull final boolean executeAsynchronously) {
        runnable.run();
    }

    @Override
    public void runInPersistenceSession(@Nonnull final Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runInTransaction(@Nonnull final Runnable runnable) {
        runnable.run();
    }
}
