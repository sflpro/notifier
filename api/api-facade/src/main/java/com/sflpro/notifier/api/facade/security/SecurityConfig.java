package com.sflpro.notifier.api.facade.security;


import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

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

    @Autowired
    private RemoteTokenServices remoteTokenServices;

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

    @PostConstruct
    private void init() {
        remoteTokenServices.setAccessTokenConverter(
                new RealAccessAuthoritySupportAccessTokenConverter(
                        accessTokenConverter()
                )
        );
    }

    @Bean
    @ConditionalOnMissingBean(AccessTokenConverter.class)
    AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
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
    PermissionNameResolver permissionNameResolver() {
        return new DefaultPermissionNameResolver(permissionMappings());
    }

    @Bean
    NotificationCreationPermissionChecker notificationCreationPermissionChecker(
            final PermissionChecker permissionChecker) {
        return new DefaultNotificationCreationPermissionChecker(
                permissionChecker,
                permissionNameResolver());
    }

    @Bean
    NotificationCreationPermissionCheckerAspect notificationCreationPermissionCheckerAspect(
            final NotificationCreationPermissionChecker notificationCreationPermissionChecker) {
        return new NotificationCreationPermissionCheckerAspect(notificationCreationPermissionChecker);
    }

    private static class RealAccessAuthoritySupportAccessTokenConverter implements AccessTokenConverter {

        private final AccessTokenConverter originalConverter;

        RealAccessAuthoritySupportAccessTokenConverter(final AccessTokenConverter originalConverter) {
            this.originalConverter = originalConverter;
        }

        @Override
        public Map<String, ?> convertAccessToken(final OAuth2AccessToken token, final OAuth2Authentication authentication) {
            return originalConverter.convertAccessToken(token, authentication);
        }

        @Override
        public OAuth2AccessToken extractAccessToken(final String value, final Map<String, ?> map) {
            return originalConverter.extractAccessToken(value, map);
        }

        @Override
        @SuppressWarnings("unchecked")
        public OAuth2Authentication extractAuthentication(final Map<String, ?> map) {
            final String authoritiesKey = "authorities";
            final Collection<String> existingAuthorities = (Collection<String>) map.get(authoritiesKey);
            if (!CollectionUtils.isEmpty(existingAuthorities)) {
                return originalConverter.extractAuthentication(map);
            }
            return Optional.ofNullable(map.get("realm_access"))
                    .filter(Map.class::isInstance)
                    .map(Map.class::cast)
                    .map(realmAccess -> realmAccess.get("roles"))
                    .filter(Collection.class::isInstance)
                    .map(Collection.class::cast)
                    .map(roles -> {
                        final Map<String, Object> extended = new HashMap<>(map);
                        extended.put(authoritiesKey, roles);
                        return originalConverter.extractAuthentication(extended);
                    })
                    .orElseGet(() -> originalConverter.extractAuthentication(map));
        }
    }
}
