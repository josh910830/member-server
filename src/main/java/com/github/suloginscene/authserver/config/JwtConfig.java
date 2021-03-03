package com.github.suloginscene.authserver.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;


@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;


    @Bean
    JwtBuilder jwtBuilder() {
        return Jwts.builder().signWith(HS256, encodedSecret());
    }

    @Bean
    JwtParser jwtParser() {
        return Jwts.parser().setSigningKey(encodedSecret());
    }

    private String encodedSecret() {
        return Base64.getEncoder()
                .encodeToString(jwtProperties.getSecret().getBytes());
    }

}
