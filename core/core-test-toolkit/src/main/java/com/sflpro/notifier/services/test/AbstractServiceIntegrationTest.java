package com.sflpro.notifier.services.test;

import com.sflpro.notifier.services.helper.ServicesTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ruben Dilanyan
 *         Aug 25, 2013
 */
@TestPropertySource("classpath:ms_notifications_integrationtest.properties")
@ContextConfiguration("classpath:applicationContext-services-integrationtest.xml")
public abstract class AbstractServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ServicesTestHelper servicesTestHelper;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractServiceIntegrationTest() {

    }

    /* Utility methods */
    protected void flush() {
        entityManager.flush();
    }

    protected void clear() {
        entityManager.clear();
    }

    protected void flushAndClear() {
        flush();
        clear();
    }

    /* Properties */
    public ServicesTestHelper getServicesTestHelper() {
        return servicesTestHelper;
    }
}
