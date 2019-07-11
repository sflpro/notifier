package com.sflpro.notifier.worker;

import com.sflpro.notifier.queue.consumer.ConsumerConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Company: SFL LLC
 * Created on 06/12/2017
 *
 * @author Davit Harutyunyan
 */
@SpringBootApplication
@PropertySource(value = "classpath:static.properties")
@Import(ConsumerConfiguration.class)
public class NotifierWorkerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NotifierWorkerApplication.class).run(args);
    }
}
