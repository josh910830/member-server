package com.github.suloginscene.authserver.config;

import com.github.suloginscene.jwtconfig.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtSecurityFilter jwtSecurityFilter;
    private final CorsConfigurationSource corsConfigurationSource;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS);

        http
                .authorizeRequests()
                .mvcMatchers(POST, "/api/members").permitAll()
                .mvcMatchers(POST, "/jwt").permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .cors().configurationSource(corsConfigurationSource);
    }

}
