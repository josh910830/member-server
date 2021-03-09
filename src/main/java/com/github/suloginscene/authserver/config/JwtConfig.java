package com.github.suloginscene.authserver.config;

import com.github.suloginscene.jjwthelper.JwtFactory;
import com.github.suloginscene.jjwthelper.JwtReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;


    @Bean
    JwtFactory jwtFactory() {
        return new JwtFactory(jwtProperties.getSecret());
    }

    @Bean
    JwtReader jwtReader() {
        return new JwtReader(jwtProperties.getSecret());
    }

}
