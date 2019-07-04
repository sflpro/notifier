package com.sflpro.notifier.queue.consumer.test;

import com.sflpro.notifier.services.helper.ServicesTestHelper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 1:33 PM
 */
@Ignore
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerTestConfiguration.class)
@TestPropertySource(properties = "notifier.queue.engine=")
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
