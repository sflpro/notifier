package com.sflpro.notifier.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@SpringBootTest(classes = {RepositoryIntegrationTestSpringBootApplication.class})
@ActiveProfiles("test")
@ContextConfiguration("classpath:applicationContext-persistence-integrationtest.xml")
@RunWith(SpringRunner.class)
public abstract class AbstractRepositoryTest {

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
