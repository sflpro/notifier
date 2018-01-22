package com.sflpro.notifier.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

/**
 * Company: SFL LLC
 * Created on 1/19/18
 *
 * @author Yervand Aghababyan
 */
@PropertySource("classpath:repository_integration_test.properties")
@EnableAutoConfiguration
@ContextConfiguration("classpath:applicationContext-persistence-integrationtest.xml")
public class ResitoryIntegrationTestContext {
}
