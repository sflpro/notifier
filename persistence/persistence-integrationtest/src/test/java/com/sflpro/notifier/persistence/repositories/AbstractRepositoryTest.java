package com.sflpro.notifier.persistence.repositories;

import com.sflpro.notifier.persistence.repositories.helper.RepositoriesTestHelper;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@ContextConfiguration("classpath:applicationContext-persistence-integrationtest.xml")
@Ignore
public class AbstractRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    /* Dependencies */
    @Autowired
    private RepositoriesTestHelper repositoriesTestHelper;

    /* Constructors */
    public AbstractRepositoryTest() {

    }

    /* Utility methods */
    protected void flush() {
        repositoriesTestHelper.flush();
    }

    protected void clear() {
        repositoriesTestHelper.clear();
    }

    protected void flushAndClear() {
        repositoriesTestHelper.flushAndClear();
    }

    /* Properties */
    protected RepositoriesTestHelper getRepositoriesTestHelper() {
        return repositoriesTestHelper;
    }
}
