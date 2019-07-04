package com.sflpro.notifier.queue.consumer.test;

import com.sflpro.notifier.services.springboot.NotifierTestApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/4/19
 * Time: 10:33 AM
 */
@Configuration
@Import(NotifierTestApplication.class)
class ConsumerTestConfiguration {
}
