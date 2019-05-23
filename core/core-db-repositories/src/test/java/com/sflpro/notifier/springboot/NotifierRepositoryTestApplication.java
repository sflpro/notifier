package com.sflpro.notifier.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Company: SFL LLC
 * Created on 1/22/18
 *
 * @author Yervand Aghababyan
 */
@SpringBootApplication
@ImportResource("classpath:applicationContext-persistence-integrationtest.xml")
public class NotifierRepositoryTestApplication {
}
