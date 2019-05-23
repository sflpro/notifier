package com.sflpro.notifier.services.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext-services-integrationtest.xml")
public class NotifierTestApplication {
}
