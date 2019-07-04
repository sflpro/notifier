package com.sflpro.notifier.services.springboot;


import com.sflpro.notifier.services.notification.impl.*;
import com.sflpro.notifier.services.notification.template.DummyTemplatingServiceImpl;
import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.spi.email.SimpleEmailSender;
import com.sflpro.notifier.spi.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import com.sflpro.notifier.spi.email.TemplatedEmailSenderRegistry;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageServiceRegistry;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SimpleSmsSenderRegistry;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;
import com.sflpro.notifier.spi.sms.TemplatedSmsSenderRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.sflpro.notifier")
@PropertySource(value = "classpath:/com/sflpro/notifier/test/services-integration-test.properties", ignoreResourceNotFound = true)
@ImportResource("classpath:/com/sflpro/notifier/test/applicationContext-mocks-integrationtest.xml")
public class NotifierTestApplication {

    @Bean
    public TemplatingService templatingService() {
        return new DummyTemplatingServiceImpl();
    }

    @Bean
    SimpleEmailSender simpleEmailSender() {
        return new DummySimpleEmailSender();
    }

    @Bean
    SimpleEmailSenderRegistry simpleEmailSenderRegistry() {
        return SimpleEmailSenderRegistry.of("smtp_server", simpleEmailSender());
    }

    @Bean
    SimpleSmsSender simpleSmsSender() {
        return new DummySimpleSmsSender();
    }

    @Bean
    SimpleSmsSenderRegistry smsSenderRegistry() {
        return SimpleSmsSenderRegistry.of("twillio", simpleSmsSender());
    }

    @Bean
    TemplatedSmsSender templatedSmsSender() {
        return new DummyTemplatedSmsSender();
    }

    @Bean
    TemplatedSmsSenderRegistry templatedSmsSenderRegistry() {
        return TemplatedSmsSenderRegistry.of("twillio", templatedSmsSender());
    }

    @Bean
    TemplatedEmailSender smtpTemplatedEmailSender() {
        return new DummyTemplatedEmailSender();
    }

    @Bean
    TemplatedEmailSender mandrillTemplatedEmailSender() {
        return new DummyTemplatedEmailSender();
    }

    @Bean
    TemplatedEmailSenderRegistry smtpTemplatedEmailSenderRegistry() {
        return TemplatedEmailSenderRegistry.of("smtp_server", smtpTemplatedEmailSender());
    }

    @Bean
    TemplatedEmailSenderRegistry mandrillTemplatedEmailSenderRegistry() {
        return TemplatedEmailSenderRegistry.of("mandrill", mandrillTemplatedEmailSender());
    }

    @Bean
    PushMessageSender pushMessageSender() {
        return new DummyPushMessageSender();
    }

    @Bean
    PushMessageSubscriber pushMessageSubscriber() {
        return new DummyPushMessageSubscriber();
    }

    @Bean
    PushMessageServiceRegistry amazonPushMessageServiceRegistry() {
        return PushMessageServiceRegistry.of("sns", pushMessageSender(), pushMessageSubscriber());
    }

}
