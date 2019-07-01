package com.sflpro.notifier.externalclients.email.mandrill;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicatorImpl;
import com.sflpro.notifier.spi.email.SimpleEmailSender;
import com.sflpro.notifier.spi.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import com.sflpro.notifier.spi.email.TemplatedEmailSenderRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;


/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:39 AM
 */
@Configuration
@ConditionalOnProperty(name = "mandrill.enabled", havingValue = "true")
class MandrillConfiguration {


    @Bean("mandrillMessagesApi")
    MandrillMessagesApi mandrillMessagesApi(@Value("${mandrill.service.token}") final String token) {
        Assert.hasText(token, "Empty or null token was provided for mandrill.");
        return new MandrillMessagesApi(token);
    }

    @Bean("mandrillApiCommunicator")
    MandrillApiCommunicator mandrillApiCommunicator(final MandrillMessagesApi mandrillMessagesApi) {
        return new MandrillApiCommunicatorImpl(mandrillMessagesApi);
    }

    @Bean("mandrillTemplatedEmailSender")
   TemplatedEmailSender mandrillTemplatedEmailSender(final MandrillApiCommunicator mandrillApiCommunicator) {
        return new MandrillTemplatedEmailSender(mandrillApiCommunicator);
    }

    @Bean("templatedEmailSenderRegistry")
    TemplatedEmailSenderRegistry templatedEmailSenderRegistry(final TemplatedEmailSender mandrillTemplatedEmailSender) {
        return TemplatedEmailSenderRegistry.of("mandrill", mandrillTemplatedEmailSender);
    }

    @Bean("mandrillSimpleEmailSender")
    SimpleEmailSender mandrillSimpleEmailSender(final MandrillApiCommunicator mandrillApiCommunicator) {
        return new MandrillSimpleEmailSender(mandrillApiCommunicator);
    }

    @Bean("mandrillSimpleEmailSenderRegistry")
    SimpleEmailSenderRegistry mandrillSimpleEmailSenderRegistry(final SimpleEmailSender mandrillSimpleEmailSender) {
        return SimpleEmailSenderRegistry.of("mandrill", mandrillSimpleEmailSender);
    }
}
