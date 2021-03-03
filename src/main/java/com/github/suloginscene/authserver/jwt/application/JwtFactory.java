package com.github.suloginscene.authserver.jwt.application;

import com.github.suloginscene.authserver.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;


@Component
@RequiredArgsConstructor
public class JwtFactory {

    private static final long EXP = 30 * 60 * 1000L;

    private final JwtProperties jwtProperties;

    // TODO register bean
    private JwtBuilder jwtBuilder;

    private String secret;


    @PostConstruct
    void encodeSecret() {
        secret = Base64.getEncoder()
                .encodeToString(jwtProperties.getSecret().getBytes());

        jwtBuilder = Jwts.builder()
                .signWith(HS256, secret);

    }


    public String create(Long userId) {
        Claims claims = Jwts.claims()
                .setAudience(userId.toString());

        Date now = new Date();
        Date exp = new Date(now.getTime() + EXP);

        return jwtBuilder
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
    }

}
