package com.sflpro.notifier.externalclients.push.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageServiceRegistry;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/5/19
 * Time: 3:02 PM
 */
@Configuration
@ConditionalOnProperty(value = "firebase.push.enabled", havingValue = "true")
class FirebaseMessagingConfiguration {

    @Bean
    @ConditionalOnMissingBean(FirebaseApp.class)
    FirebaseApp firebaseApp(@Value("${firebase.serviceAccount.resource.path:classpath:com/sflpro/notifier/externalclients/push/firebase/notifier-development-firebase-adminsdk-629bf-582f1cff8f.json}") final String serviceAccountResourcePath) {
        try (final InputStream input = serviceAccountResourceStream(serviceAccountResourcePath)) {
            return FirebaseApp.initializeApp(new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(input))
                    .build());
        } catch (final IOException ex) {
            throw new FirebaseAppInitializationFailedException("Unable to initialize firebase app.", ex);
        }
    }

    @Bean
    FirebaseMessaging firebaseMessaging(final FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    @ConfigurationProperties(prefix = "push.android.default")
    Properties defaultAndroidConfig(final Environment environment) {
        return new Properties();
    }

    @Bean
    @ConfigurationProperties(prefix = "push.apns.default")
    Properties defaultApnsConfig(final Environment environment) {
        return new Properties();
    }

    @Bean
    PushMessageSender firebasePushMessageSender(final FirebaseMessaging firebaseMessaging, final Properties defaultAndroidConfig, final Properties defaultApnsConfig) {
        return new FirebasePushMessageSender(firebaseMessaging, defaultAndroidConfig, defaultApnsConfig);
    }

    @Bean
    PushMessageSubscriber firebasePushMessageSubscriber() {
        return new FirebasePushMessageSubscriber();
    }

    @Bean
    PushMessageServiceRegistry firebasePushMessageServiceRegistry(final PushMessageSender firebasePushMessageSender, final PushMessageSubscriber firebasePushMessageSubscriber) {
        return PushMessageServiceRegistry.of("fcm", firebasePushMessageSender, firebasePushMessageSubscriber);
    }

    private InputStream serviceAccountResourceStream(final String serviceAccountResourcePath) {
        try {
            return new DefaultResourceLoader().getResource(serviceAccountResourcePath).getInputStream();
        } catch (final IOException ex) {
            throw new FirebaseAppInitializationFailedException("Service account file is missing", ex);
        }
    }

    private static final class FirebaseAppInitializationFailedException extends RuntimeException {
        FirebaseAppInitializationFailedException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
