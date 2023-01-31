package com.sergio.auth.backend.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    ReactiveClientRegistrationRepository clientRegistrations() {
        ClientRegistration clientRegistration = ClientRegistrations
                .fromOidcIssuerLocation("https://sso.billview.com.au/realms/myfirsttest")
                .clientId("homedemo1")
                .clientSecret("Tazr42CrDbtuqS21zLNeQOcQaT3KyhYL")
                .redirectUri("https://backend-gateway-client:8543/login/oauth2/code/{registrationId}")
                .scope("openid", "email", "profile", "roles", "message.read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }

    /**
     * https://docs.spring.io/spring-security/site/docs/5.1.5.RELEASE/reference/html/webflux-oauth2.html
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        log.info("disabling csrf");
        http.csrf().disable()
                .oauth2Client();
        return http.build();
    }

}
