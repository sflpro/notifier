package com.sflpro.notifier.api.web;

import com.sflpro.notifier.DirectNotificationProcessorConfiguration;
import com.sflpro.notifier.queue.producer.ProducerConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/**
 * Company: SFL LLC
 * Created on 03/12/2017
 *
 * @author Davit Harutyunyan
 */
@SpringBootApplication
@PropertySource(value = "static.properties")
@Import({ProducerConfiguration.class, DirectNotificationProcessorConfiguration.class})
@ComponentScan(basePackages = "com.sflpro.notifier")
public class NotifierApiApplication {

    @Bean
    @ConfigurationProperties(prefix = "")
    Properties allProps() {
        return new Properties();
    }

    public static void main(String[] args) {
        final Properties allProps = (Properties) new SpringApplicationBuilder(NotifierApiApplication.class).run(args).getBean("allProps");
        System.out.println("----------------");
        allProps.entrySet().stream()
                .map(objectObjectEntry -> org.apache.commons.lang3.tuple.Pair.of(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString()))
                .filter(objectObjectEntry -> !Character.isUpperCase(objectObjectEntry.getKey().charAt(0)))
                .forEach(stringStringPair -> System.out.println(stringStringPair.getKey() + " = " + stringStringPair.getValue()));

    }
}