package com.project.authorizationserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerConfig(HttpSecurity http, RegisteredClientRepository registeredClientRepository) throws Exception {
        http.cors(corsConfig -> corsConfig.configurationSource(null));
        var authorizationConfig = OAuth2AuthorizationServerConfigurer.authorizationServer()
                .tokenGenerator(null)
                .clientAuthentication(authentication -> {
                    authentication.authenticationConverter(null);
                    authentication.authenticationProvider(null);
                })
                .oidc(Customizer.withDefaults());
        http
                .securityMatcher(authorizationConfig.getEndpointsMatcher())
                .with(authorizationConfig, Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions.accessDeniedPage("/accessdenied")
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }



}
