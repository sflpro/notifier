package com.sflpro.notifier.services.springboot;


import com.sflpro.notifier.email.SimpleEmailSender;
import com.sflpro.notifier.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.email.TemplatedEmailSender;
import com.sflpro.notifier.email.TemplatedEmailSenderRegistry;
import com.sflpro.notifier.services.notification.impl.DummySimpleEmailSender;
import com.sflpro.notifier.services.notification.impl.DummySmsSender;
import com.sflpro.notifier.services.notification.impl.DummyTemplatedEmailSender;
import com.sflpro.notifier.services.notification.template.DummyTemplatingServiceImpl;
import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.sms.SmsSender;
import com.sflpro.notifier.sms.SmsSenderRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.sflpro.notifier")
@PropertySource(value = "classpath:/com/sflpro/notifier/test/services-integration-test.properties", ignoreResourceNotFound = true)
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
    SmsSender smsSender() {
        return new DummySmsSender();
    }

    @Bean
    SmsSenderRegistry smsSenderRegistry() {
        return SmsSenderRegistry.of("twillio", smsSender());
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
}
