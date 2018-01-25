package com.sflpro.notifier.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * Company: SFL LLC
 * Created on 1/22/18
 *
 * @author Yervand Aghababyan
 */
@SpringBootApplication
@EnableAutoConfiguration
@PropertySource({"classpath:repositories_integration_test.properties", "classpath:repositories.properties"})
public class RepositoryIntegrationTestSpringBootApplication {
}
