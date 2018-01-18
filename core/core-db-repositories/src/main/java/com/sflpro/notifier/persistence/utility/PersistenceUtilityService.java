package com.sflpro.notifier.persistence.utility;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/15/14
 * Time: 9:49 AM
 */
public interface PersistenceUtilityService {

    /**
     * UnProxies entity. Useful when using lazy initialization with class hierarchies during persistence
     *
     * @param entity entity to be unProxies
     * @return unProxied entity
     */
    @Nonnull
    <T> T initializeAndUnProxy(@Nonnull final T entity);


    /**
     * Wrap task running in persistence session
     *
     * @param runnable
     */
    void runInPersistenceSession(@Nonnull final Runnable runnable);

    /**
     * Runs task in new transaction
     *
     * @param runnable
     */
    void runInNewTransaction(@Nonnull final Runnable runnable);

    /**
     * Runs task in transaction
     *
     * @param runnable
     */
    void runInTransaction(@Nonnull final Runnable runnable);

    /**
     * Runs task in after current transaction successfully commits
     *
     * @param runnable
     * @param executeAsynchronously;
     */
    void runAfterTransactionCommitIsSuccessful(@Nonnull final Runnable runnable, final boolean executeAsynchronously);
}
