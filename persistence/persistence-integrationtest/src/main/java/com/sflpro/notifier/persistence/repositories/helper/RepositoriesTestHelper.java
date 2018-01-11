package com.sflpro.notifier.persistence.repositories.helper;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@Component
public class RepositoriesTestHelper {

    /* Dependencies */

    @PersistenceContext
    private EntityManager entityManager;

    public RepositoriesTestHelper() {
    }

    /* Utility methods */
    public void flush() {
        entityManager.flush();
    }

    public void clear() {
        entityManager.clear();
    }

    public void flushAndClear() {
        flush();
        clear();
    }

}
