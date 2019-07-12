package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.common.http.rest.RestClientImpl;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.DefaultNikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.spi.sms.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:49 AM
 */
@Configuration
@ConditionalOnProperty(name = "nikitamobile.enabled", havingValue = "true")
class NikitamobileConfiguration {

    private static final String NIKITA_MOBILE_PROVIDER_REGISTRY_NAME = "nikita_mobile";

    @Value("${nikitamobile.operator.id}")
    private String operatorId;
    @Value("${nikitamobile.operator.name}")
    private String operatorName;

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    RestClient restClient(final RestTemplate restTemplate) {
        return new RestClientImpl(restTemplate);
    }

    @Bean("nikitamobileApiCommunicator")
    NikitamobileApiCommunicator nikitamobileApiCommunicator(final RestClient restClient, @Value("${nikitamobile.api.url:}") final String apiUrl) {
        Assert.hasText(apiUrl, "API url is missing from Nikitamobile configuration.");
        return new DefaultNikitamobileApiCommunicator(restClient, apiUrl);
    }

    @Bean("nikitamobileSimpleSmsSender")
    SimpleSmsSender nikitamobileSimpleSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator) {
        return new NikitamobileSimpleSmsSender(nikitamobileApiCommunicator, operatorId, operatorName);
    }

    @Bean("nikitamobileSimpleSmsSender")
    SimpleSmsSenderRegistry nikitamobileSimpleSmsSenderRegistry(final SimpleSmsSender nikitamobileSimpleSmsSender) {
        return SimpleSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileSimpleSmsSender);
    }

    @Bean("nikitamobileSimpleSmsSender")
    @ConditionalOnBean(SmsTemplateContentResolver.class)
    TemplatedSmsSender nikitamobileTemplatedSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator,
                                                      final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new NikitamobileTemplatedSmsSender(nikitamobileApiCommunicator, smsTemplateContentResolver, operatorId, operatorName);
    }

    @Bean("nikitamobileSimpleSmsSender")
    @ConditionalOnBean(name = "nikitamobileTemplatedSmsSender")
    TemplatedSmsSenderRegistry nikitamobileTemplatedSmsSenderRegistry(final TemplatedSmsSender nikitamobileTemplatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileTemplatedSmsSender);
    }
}
