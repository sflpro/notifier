package com.sflpro.notifier.test;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@ContextConfiguration(classes = ResitoryIntegrationTestContext.class)
@Ignore
public abstract class AbstractRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    /* Dependencies */
    @PersistenceContext
    private EntityManager em;

    /* Constructors */
    public AbstractRepositoryTest() {

    }

    /* Utility methods */
    protected void flush() {
        em.flush();
    }

    protected void clear() {
        em.clear();
    }

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
