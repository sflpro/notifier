package com.sflpro.notifier.externalclients.sms.msgam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.common.http.rest.RestClientImpl;
import com.sflpro.notifier.externalclients.sms.msgam.client.MsgAmRestClient;
import com.sflpro.notifier.externalclients.sms.msgam.client.MsgAmRestClientImpl;
import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicator;
import com.sflpro.notifier.externalclients.sms.msgam.communicator.MsgAmApiCommunicatorImpl;
import com.sflpro.notifier.spi.sms.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 5:39 PM
 */
@Configuration
@ConditionalOnProperty(name = MsgAmConfiguration.LOGIN_PROP_NAME)
@SuppressWarnings("unused")
class MsgAmConfiguration {

    private static final String MSGAM_PROVIDER_REGISTRY_NAME = "msg_am";

    static final String LOGIN_PROP_NAME = "msgam.account.login";


    @Bean("restClient_msgAmRestClient")
    RestClient restClientForMsgAm() {
        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.xml()
                .modules(new JaxbAnnotationModule())
                .build();
        final RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(
                        new MappingJackson2XmlHttpMessageConverter(objectMapper),
                        new HtmlToXmlHttpMessageConverter(objectMapper)).build();
        return new RestClientImpl(restTemplate);
    }

    @Bean("msgAmRestClient")
    MsgAmRestClient msgAmRestClient(final Environment environment) {
        return new MsgAmRestClientImpl(
                environment.getProperty("msgam.url"),
                restClientForMsgAm()
        );
    }

    @Bean("msgAmApiCommunicator")
    MsgAmApiCommunicator msgAmApiCommunicator(final MsgAmRestClient msgAmRestClient, final Environment environment) {
        return new MsgAmApiCommunicatorImpl(
                environment.getProperty(LOGIN_PROP_NAME),
                environment.getProperty("msgam.account.pass"),
                msgAmRestClient);
    }


    @Bean("msgAmSimpleSmsSender")
    SimpleSmsSender msgAmSimpleSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator) {
        return new MsgAmSimpleSmsSender(msgAmApiCommunicator);
    }

    @Bean("msgAmSimpleSmsSenderRegistry")
    SimpleSmsSenderRegistry msgAmSimpleSmsSenderRegistry(final SimpleSmsSender msgAmSimpleSmsSender) {
        return SimpleSmsSenderRegistry.of(MSGAM_PROVIDER_REGISTRY_NAME, msgAmSimpleSmsSender);
    }

    @Bean("msgAmTemplatedSmsSender")
    @ConditionalOnBean(SmsTemplateContentResolver.class)
    TemplatedSmsSender msgAmTemplatedSmsSender(final MsgAmApiCommunicator msgAmApiCommunicator, final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new MsgAmTemplatedSmsSender(msgAmApiCommunicator, smsTemplateContentResolver);
    }

    @Bean("msgAmSimpleSmsSenderRegistry")
    @ConditionalOnBean(name = "msgAmTemplatedSmsSender")
    TemplatedSmsSenderRegistry msgAmSimpleSmsSenderRegistry(final TemplatedSmsSender msgAmTemplatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(MSGAM_PROVIDER_REGISTRY_NAME, msgAmTemplatedSmsSender);
    }


    private static class HtmlToXmlHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

        HtmlToXmlHttpMessageConverter(final ObjectMapper objectMapper) {
            super(objectMapper, new MediaType("text", "html"));
        }
    }
}
