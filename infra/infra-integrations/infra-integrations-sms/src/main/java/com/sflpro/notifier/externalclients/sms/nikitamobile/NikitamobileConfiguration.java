package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.common.http.rest.RestClientImpl;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.DefaultNikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.spi.sms.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:49 AM
 */
@Configuration
@ConditionalOnProperty(name = "nikitamobile.enabled", havingValue = "true")
class NikitamobileConfiguration {

    private static final String NIKITA_MOBILE_PROVIDER_REGISTRY_NAME = "nikita_mobile";

    @Value("${nikitamobile.login}")
    private String login;

    @Value("${nikitamobile.password}")
    private String password;

    @Value("${nikitamobile.api.version:1.0}")
    private String version;

    @Bean("restClient_nikitamobileRestClient")
    RestClient restClient_nikitamobileRestClient() {
        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.xml()
                .modules(new JaxbAnnotationModule())
                .build();
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Collections.singletonList( new MappingJackson2XmlHttpMessageConverter(objectMapper)));
        return new RestClientImpl(restTemplate);
    }



    @Bean("nikitamobileApiCommunicator")
    NikitamobileApiCommunicator nikitamobileApiCommunicator(@Value("${nikitamobile.api.url:}") final String apiUrl) {
        Assert.hasText(apiUrl, "API url is missing from Nikitamobile configuration.");
        return new DefaultNikitamobileApiCommunicator(restClient_nikitamobileRestClient(), apiUrl);
    }

    @Bean("nikitamobileSimpleSmsSender")
    SimpleSmsSender nikitamobileSimpleSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator) {
        return new NikitamobileSimpleSmsSender(nikitamobileApiCommunicator, login, password, version);
    }

    @Bean("nikitamobileSimpleSmsSender")
    SimpleSmsSenderRegistry nikitamobileSimpleSmsSenderRegistry(final SimpleSmsSender nikitamobileSimpleSmsSender) {
        return SimpleSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileSimpleSmsSender);
    }

    @Bean("nikitamobileSimpleSmsSender")
    TemplatedSmsSender nikitamobileTemplatedSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator,
                                                      final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new NikitamobileTemplatedSmsSender(nikitamobileApiCommunicator, smsTemplateContentResolver, login, password, version);
    }

    @Bean("nikitamobileSimpleSmsSender")
    TemplatedSmsSenderRegistry nikitamobileTemplatedSmsSenderRegistry(final TemplatedSmsSender nikitamobileTemplatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileTemplatedSmsSender);
    }
}
