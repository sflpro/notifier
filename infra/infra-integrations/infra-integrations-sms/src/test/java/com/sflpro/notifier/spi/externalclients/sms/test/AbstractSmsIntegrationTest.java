package com.sflpro.notifier.externalclients.sms.test;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/15/15
 * Time: 10:50 AM
 */
@Ignore
@ContextConfiguration(value = {"classpath:applicationContext-externalclients-sms-integrationtest.xml"})
public class AbstractSmsIntegrationTest extends AbstractJUnit4SpringContextTests {

    /* Dependencies */

    /* Constructors */
    public AbstractSmsIntegrationTest() {
    }

    /* Utility methods */
}
