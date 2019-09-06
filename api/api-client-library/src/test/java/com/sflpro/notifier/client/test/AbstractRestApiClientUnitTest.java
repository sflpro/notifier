package com.sflpro.notifier.client.test;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 5/21/15
 * Time: 12:28 PM
 */
@RunWith(EasyMockRunner.class)
@Ignore
public abstract class AbstractRestApiClientUnitTest extends EasyMockSupport {


    public AbstractRestApiClientUnitTest() {
    }

    /* Utility methods */

    /* Getters and setters */
    public static String uuid(){
        return UUID.randomUUID().toString();
    }

}
