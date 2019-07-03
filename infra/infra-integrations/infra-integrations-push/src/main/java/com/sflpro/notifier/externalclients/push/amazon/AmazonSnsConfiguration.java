package com.sflpro.notifier.externalclients.push.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicatorImpl;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageServiceRegistry;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 11:20 AM
 */
@Configuration
@ConditionalOnProperty(value = "amazon.sns.enabled", havingValue = "true")
class AmazonSnsConfiguration {

    @Bean
    AmazonSNSClient amazonSNSClient(@Value("${amazon.account.sns.accesskey}") final String accessKey,
                                    @Value("${amazon.account.sns.secretkey}") final String secretKey,
                                    @Value("${amazon.account.sns.region}") final String awsRegion) {
        final AmazonSNSClient amazonSNSClient = new AmazonSNSClient(new BasicAWSCredentials(accessKey, secretKey));
        amazonSNSClient.setRegion(Region.getRegion(Regions.valueOf(awsRegion)));
        return amazonSNSClient;
    }

    @Bean
    AmazonSnsApiCommunicator amazonSnsApiCommunicator(final AmazonSNSClient amazonSNSClient,
                                                      @Value("${amazon.account.sns.development}") final boolean amazonSnsDevelopmentMode) {
        return new AmazonSnsApiCommunicatorImpl(amazonSNSClient, amazonSnsDevelopmentMode);
    }

    @Bean
    PushMessageSender amazonSnsPushMessageSender(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        return new AmazonSnsPushMessageSender(amazonSnsApiCommunicator);
    }

    @Bean
    PushMessageSubscriber amazonSnsPushMessageSubscriber(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        return new AmazonSnsPushMessageSubscriber(amazonSnsApiCommunicator);
    }

    @Bean
    PushMessageServiceRegistry amazonSnsPushMessageServiceRegistry(final PushMessageSender amazonPushMessageSender, final PushMessageSubscriber amazonPushMessageSubscriber) {
        return PushMessageServiceRegistry.of("sns", amazonPushMessageSender, amazonPushMessageSubscriber);
    }
}
