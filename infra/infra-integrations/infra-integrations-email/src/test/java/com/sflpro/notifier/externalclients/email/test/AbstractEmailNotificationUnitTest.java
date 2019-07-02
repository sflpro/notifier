package com.sflpro.notifier.externalclients.email.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractEmailNotificationUnitTest {


    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
