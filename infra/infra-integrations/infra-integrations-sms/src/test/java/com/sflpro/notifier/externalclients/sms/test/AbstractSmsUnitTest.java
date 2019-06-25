package com.sflpro.notifier.externalclients.sms.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 5:24 PM
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractSmsUnitTest {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
