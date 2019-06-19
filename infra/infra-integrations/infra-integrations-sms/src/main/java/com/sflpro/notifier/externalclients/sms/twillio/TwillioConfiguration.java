package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicatorImpl;
import com.sflpro.notifier.sms.SmsSender;
import com.sflpro.notifier.sms.SmsSenderRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:07 PM
 */
@Configuration
@ConditionalOnProperty(name = "twillio.account.sid")
@SuppressWarnings("unused")
class TwillioConfiguration {


    @Bean("twillioApiCommunicator")
    TwillioApiCommunicator twillioApiCommunicator() {
        return new TwillioApiCommunicatorImpl();
    }

    @Bean("twillioSmsSender")
    SmsSender twillioSmsSender() {
        return new TwillioSmsSender(twillioApiCommunicator());
    }

    @Bean("twillioSmsSenderRegistry")
    SmsSenderRegistry twillioSmsSenderRegistry() {
        return SmsSenderRegistry.of("twillio", twillioSmsSender());
    }

}
