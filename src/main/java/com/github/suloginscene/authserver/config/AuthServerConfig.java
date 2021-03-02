package com.github.suloginscene.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;


@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final ClientProperties clientProperties;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String id = clientProperties.getId();
        String secret = clientProperties.getSecret();

        clients.inMemory()
                .withClient(id)
                .secret(passwordEncoder.encode(secret))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }

}
