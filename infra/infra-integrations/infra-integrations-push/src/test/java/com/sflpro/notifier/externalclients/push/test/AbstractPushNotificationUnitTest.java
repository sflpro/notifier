package com.sflpro.notifier.externalclients.push.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 2:30 PM
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractPushNotificationUnitTest {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
