package com.sflpro.notifier.externalclients.email.mandrill;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.sflpro.notifier.email.TemplatedEmailSenderRegistry;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicatorImpl;
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
@ConditionalOnProperty(name = MandrillConfiguration.TOKEN_PLACE_HOLDER)
class MandrillConfiguration {

    static final String TOKEN_PLACE_HOLDER = "mandrill.service.token";

    @Bean("mandrillMessagesApi")
    MandrillMessagesApi mandrillMessagesApi(@Value("${" + TOKEN_PLACE_HOLDER + "}") final String token) {
        Assert.hasText(token, "Empty or null token was provided for mandrill.");
        return new MandrillMessagesApi(token);
    }

    @Bean("mandrillApiCommunicator")
    MandrillApiCommunicator mandrillApiCommunicator(final MandrillMessagesApi mandrillMessagesApi) {
        return new MandrillApiCommunicatorImpl(mandrillMessagesApi);
    }

    @Bean("mandrillTemplatedEmailSender")
    MandrillTemplatedEmailSender mandrillTemplatedEmailSender(final MandrillApiCommunicator mandrillApiCommunicator) {
        return new MandrillTemplatedEmailSender(mandrillApiCommunicator);
    }

    @Bean("templatedEmailSenderRegistry")
    TemplatedEmailSenderRegistry templatedEmailSenderRegistry(final MandrillTemplatedEmailSender mandrillTemplatedEmailSender) {
        return TemplatedEmailSenderRegistry.of("mandrill", mandrillTemplatedEmailSender);
    }
}
