package com.github.suloginscene.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtSecurityFilter jwtSecurityFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .mvcMatchers(POST, "/api/members")
                .mvcMatchers(POST, "/jwt");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS);

        http
                .authorizeRequests().anyRequest().authenticated();

        http
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
