package com.sflpro.notifier.db.repositories.utility.impl;

import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/15/14
 * Time: 9:50 AM
 */
public class PersistenceUtilityServiceImpl implements PersistenceUtilityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceUtilityServiceImpl.class);

    /* Constants */
    private static final int MAX_THREADS_COUNT = 25;

    /* Spring beans */
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private ExecutorService executorService;

    public PersistenceUtilityServiceImpl() {
        // Initialize thread pool
        initExecutorService();
    }

    @Override
    public void runInPersistenceSession(@Nonnull final Runnable runnable) {
        assertRunnable(runnable);
        boolean participate = false;
        if (TransactionSynchronizationManager.hasResource(entityManagerFactory)) {
            // Do not modify the EntityManager: just set the participate flag.
            participate = true;
        } else {
            LOGGER.debug("Opening JPA EntityManager in Spring Job");
            try {
                final EntityManager em = entityManagerFactory.createEntityManager();
                TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(em));
            } catch (PersistenceException ex) {
                throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
            }
        }
        try {
            runnable.run();
        } finally {
            if (!participate) {
                EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
                LOGGER.debug("Closing JPA EntityManager in Spring Job");
                EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void runInNewTransaction(@Nonnull final Runnable runnable) {
        assertRunnable(runnable);
        LOGGER.debug("Running task in new transaction");
        runnable.run();
    }

    @Transactional
    @Override
    public void runInTransaction(@Nonnull final Runnable runnable) {
        assertRunnable(runnable);
        LOGGER.debug("Running task in transaction");
        runnable.run();
    }

    @Override
    public void runAfterTransactionCommitIsSuccessful(@Nonnull final Runnable runnable, final boolean executeAsynchronously) {
        assertRunnable(runnable);
        LOGGER.debug("Registering task as commit synchronization, asynchronous - {}", executeAsynchronously);
        TransactionSynchronizationManager.registerSynchronization(new SuccessfulCommitTask(runnable, executeAsynchronously));
    }

    /* Utility methods */
    private void initExecutorService() {
        LOGGER.debug("Initializing executor service using thread pool size - {}", MAX_THREADS_COUNT);
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
    }

    private void assertRunnable(final Runnable runnable) {
        Assert.notNull(runnable, "Runnable should not be null");
    }

    /* Inner class */
    private class SuccessfulCommitTask extends TransactionSynchronizationAdapter {

        /* Properties */
        private final Runnable runnable;

        private final boolean executeAsynchronously;

        /* Constructors */
        public SuccessfulCommitTask(final Runnable runnable, final boolean executeAsynchronously) {
            this.runnable = runnable;
            this.executeAsynchronously = executeAsynchronously;
        }

        @Override
        public void afterCommit() {
            if (executeAsynchronously) {
                executorService.submit(() -> runInPersistenceSession(runnable));
            } else {
                runnable.run();
            }
        }
    }
}
