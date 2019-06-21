package com.sflpro.notifier.queue.consumer.test;

import com.sflpro.notifier.services.helper.ServicesTestHelper;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 1:33 PM
 */
@Ignore
@ContextConfiguration(value = {"src/test/resources/applicationContext-queue-consumer-services-integrationtest.xml", "classpath:applicationContext-services-integrationtest.xml"})
public class AbstractQueueConsumerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    /* Dependencies */
    @Autowired
    private ServicesTestHelper servicesTestHelper;

    @PersistenceContext
    private EntityManager entityManager;

    /* Constructors */
    public AbstractQueueConsumerIntegrationTest() {
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

    /* Properties getters and setters */
    public ServicesTestHelper getServicesTestHelper() {
        return servicesTestHelper;
    }
}
