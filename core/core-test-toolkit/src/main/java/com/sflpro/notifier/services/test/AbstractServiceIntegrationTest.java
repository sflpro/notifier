package com.sflpro.notifier.services.test;

import com.sflpro.notifier.services.helper.ServicesTestHelper;
import com.sflpro.notifier.services.springboot.NotifierTestApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ruben Dilanyan
 *         Aug 25, 2013
 */
@ActiveProfiles("test")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifierTestApplication.class)
public abstract class AbstractServiceIntegrationTest {

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
