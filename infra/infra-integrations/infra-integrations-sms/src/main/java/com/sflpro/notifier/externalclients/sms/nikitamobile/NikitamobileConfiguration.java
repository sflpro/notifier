package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.common.http.rest.RestClientImpl;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.DefaultNikitamobileApiCommunicator;
import com.sflpro.notifier.externalclients.sms.nikitamobile.communicator.NikitamobileApiCommunicator;
import com.sflpro.notifier.spi.sms.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
        final Jaxb2RootElementHttpMessageConverter jaxbMessageConverter = new Jaxb2RootElementHttpMessageConverter();
        jaxbMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_XML,
                MediaType.TEXT_XML,
                new MediaType(MediaType.APPLICATION_XML, StandardCharsets.US_ASCII),
                new MediaType(MediaType.APPLICATION_XHTML_XML, StandardCharsets.US_ASCII)
        ));
        jaxbMessageConverter.setDefaultCharset(StandardCharsets.US_ASCII);
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Collections.singletonList(jaxbMessageConverter));
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

    @Bean("nikitamobileSimpleSmsSenderRegistry")
    SimpleSmsSenderRegistry nikitamobileSimpleSmsSenderRegistry(final SimpleSmsSender nikitamobileSimpleSmsSender) {
        return SimpleSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileSimpleSmsSender);
    }

    @Bean("nikitamobileTemplatedSmsSender")
    TemplatedSmsSender nikitamobileTemplatedSmsSender(final NikitamobileApiCommunicator nikitamobileApiCommunicator,
                                                      final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new NikitamobileTemplatedSmsSender(nikitamobileApiCommunicator, smsTemplateContentResolver, login, password, version);
    }

    @Bean("nikitamobileTemplatedSmsSenderRegistry")
    TemplatedSmsSenderRegistry nikitamobileTemplatedSmsSenderRegistry(final TemplatedSmsSender nikitamobileTemplatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(NIKITA_MOBILE_PROVIDER_REGISTRY_NAME, nikitamobileTemplatedSmsSender);
    }
}
