package com.sflpro.notifier.externalclients.sms.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.externalclients.common.http.rest.RestClient;
import com.sflpro.notifier.externalclients.common.http.rest.RestClientImpl;
import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClient;
import com.sflpro.notifier.externalclients.sms.webhook.client.WebhookApiRestClientImpl;
import com.sflpro.notifier.spi.sms.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 4:04 PM
 */
@Configuration
@ConditionalOnProperty(name = "webhook.enabled", havingValue = "true")
class WebhookSmsSenderConfiguration {

    private static final String WEBHOOK_PROVIDER_REGISTRY_NAME = "webhook";

    @Bean("restClient_webhookRestClient")
    RestClient restClient() {
        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .build();
        final RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();
        return new RestClientImpl(restTemplate);
    }

    @Bean
    WebhookApiRestClient webhookApiRestClient(final Environment environment) {
        return new WebhookApiRestClientImpl(environment.getProperty("webhook.url"), restClient());
    }

    @Bean("webhookSimpleSmsSender")
    SimpleSmsSender webhookSimpleSmsSender(final WebhookApiRestClient apiRestClient) {
        return new WebhookSimpleSmsSender(apiRestClient);
    }

    @Bean("webhookSimpleSmsSenderRegistry")
    SimpleSmsSenderRegistry webhookSimpleSmsSenderRegistry(final SimpleSmsSender webhookSimpleSmsSender) {
        return SimpleSmsSenderRegistry.of(WEBHOOK_PROVIDER_REGISTRY_NAME, webhookSimpleSmsSender);
    }

    @Bean("webhookTemplatedSmsSender")
    TemplatedSmsSender webhookTemplatedSmsSender(final WebhookApiRestClient apiRestClient, final SmsTemplateContentResolver smsTemplateContentResolver) {
        return new WebhookTemplatedSmsSender(apiRestClient, smsTemplateContentResolver);
    }

    @Bean("webhookTemplatedSmsSenderRegistry")
    TemplatedSmsSenderRegistry webhookTemplatedSmsSenderRegistry(final TemplatedSmsSender templatedSmsSender) {
        return TemplatedSmsSenderRegistry.of(WEBHOOK_PROVIDER_REGISTRY_NAME, templatedSmsSender);
    }
}
