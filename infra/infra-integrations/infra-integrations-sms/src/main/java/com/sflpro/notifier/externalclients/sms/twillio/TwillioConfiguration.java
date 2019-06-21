package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicatorImpl;
import com.sflpro.notifier.sms.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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


    private static final String TWILLIO_PROVIDER_REGISTRY_NAME = "twillio";

    @Bean("twillioApiCommunicator")
    TwillioApiCommunicator twillioApiCommunicator() {
        return new TwillioApiCommunicatorImpl();
    }

    @Bean("twillioSmsSender")
    SimpleSmsSender twillioSmsSender() {
        return new TwillioSimpleSmsSender(twillioApiCommunicator());
    }

    @Bean("twillioSmsSenderRegistry")
    SimpleSmsSenderRegistry twillioSmsSenderRegistry() {
        return SimpleSmsSenderRegistry.of(TWILLIO_PROVIDER_REGISTRY_NAME, twillioSmsSender());
    }

    @Bean("twillioTemplatedSmsSender")
    @ConditionalOnBean(SmsTemplateContentResolver.class)
    TemplatedSmsSender twillioTemplatedSmsSender(final SmsTemplateContentResolver smsTemplateContentResolver){
        return new TwillioTemplatedSmsSender(twillioApiCommunicator(),smsTemplateContentResolver);
    }

    @Bean("twillioTemplatedSmsSenderRegistry")
    @ConditionalOnBean(name = "twillioTemplatedSmsSender")
    TemplatedSmsSenderRegistry twillioTemplatedSmsSenderRegistry(final TemplatedSmsSender twillioTemplatedSmsSender){
        return TemplatedSmsSenderRegistry.of(TWILLIO_PROVIDER_REGISTRY_NAME,twillioTemplatedSmsSender);
    }

}
