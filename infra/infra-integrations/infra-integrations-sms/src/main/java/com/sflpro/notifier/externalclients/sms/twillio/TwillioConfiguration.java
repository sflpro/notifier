package com.sflpro.notifier.externalclients.sms.twillio;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.DefaultTwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.spi.sms.*;
import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:07 PM
 */
@Configuration
@ConditionalOnProperty(name = "twillio.enabled")
@SuppressWarnings("unused")
class TwillioConfiguration {


    private static final String TWILLIO_PROVIDER_REGISTRY_NAME = "twillio";

    @Bean("twillioApiCommunicator")
    TwillioApiCommunicator twillioApiCommunicator(@Value("${twillio.account.authToken:}") final String accountAuthToken,
                                                  @Value("${twillio.account.sid:}") final String accountSid) {
        return new DefaultTwillioApiCommunicator(
                new TwilioRestClient.Builder(accountSid, accountAuthToken).build()
        );
    }

    @Bean("twillioSmsSender")
    SimpleSmsSender twillioSmsSender(final TwillioApiCommunicator twillioApiCommunicator) {
        return new TwillioSimpleSmsSender(twillioApiCommunicator);
    }

    @Bean("twillioSmsSenderRegistry")
    SimpleSmsSenderRegistry twillioSmsSenderRegistry(final SimpleSmsSender twillioSmsSender) {
        return SimpleSmsSenderRegistry.of(TWILLIO_PROVIDER_REGISTRY_NAME, twillioSmsSender);
    }

    @Bean("twillioTemplatedSmsSender")
    TemplatedSmsSender twillioTemplatedSmsSender(final TwillioApiCommunicator twillioApiCommunicator, final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new TwillioTemplatedSmsSender(twillioApiCommunicator, smsTemplateContentResolver);
    }

    @Bean("twillioTemplatedSmsSenderRegistry")
    TemplatedSmsSenderRegistry twillioTemplatedSmsSenderRegistry(final TemplatedSmsSender twillioTemplatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(TWILLIO_PROVIDER_REGISTRY_NAME, twillioTemplatedSmsSender);
    }

}
