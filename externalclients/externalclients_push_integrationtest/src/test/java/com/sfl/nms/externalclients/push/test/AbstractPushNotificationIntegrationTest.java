package com.sfl.nms.externalclients.push.test;

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
@ContextConfiguration(value = {"classpath:applicationContext-externalclients-push-integrationtest.xml"})
public class AbstractPushNotificationIntegrationTest extends AbstractJUnit4SpringContextTests {

    /* Dependencies */

    /* Constructors */
    public AbstractPushNotificationIntegrationTest() {
    }

    /* Utility methods */
}
