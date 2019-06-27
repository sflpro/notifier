package com.sflpro.notifier.api.facade.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/25/19
 * Time: 2:41 PM
 */
@Configuration
@ConditionalOnProperty(value = "security.enabled", havingValue = "true")
@EnableAspectJAutoProxy
@EnableWebSecurity
@EnableOAuth2Sso
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .addFilterBefore(new AutoAuthenticationFilter() {
                    @Override
                    protected TokenExtractor tokenExtractor() {
                        return SecurityConfig.this.tokenExtractor();
                    }
                }, AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/notification/*/create")
                .authenticated();
    }

    @Bean
    @ConditionalOnMissingBean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(BearerTokenExtractor.class)
    TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor();
    }

    @Bean
    PermissionChecker permissionChecker(final RemoteTokenServices remoteTokenServices) {
        return new DefaultPermissionChecker(remoteTokenServices);
    }

    @Bean
    @ConfigurationProperties(prefix = "permission.name")
    Properties permissionMappings() {
        return new Properties();
    }

    @Bean
    NotificationCreationPermissionChecker notificationCreationPermissionChecker(
            final PermissionChecker permissionChecker,
            @Value("${permission.non.templated.notification}") final String nonTemplatedNotificationCreationPermission,
            final Properties permissionMappings) {
        return new DefaultNotificationCreationPermissionChecker(
                permissionChecker,
                nonTemplatedNotificationCreationPermission,
                permissionMappings);
    }

    @Bean
    NotificationCreationPermissionCheckerAspect notificationCreationPermissionCheckerAspect(final NotificationCreationPermissionChecker notificationCreationPermissionChecker) {
        return new NotificationCreationPermissionCheckerAspect(notificationCreationPermissionChecker);
    }
}
