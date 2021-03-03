package com.github.suloginscene.authserver.jwt.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtFactory {

    private static final long EXP = 30 * 60 * 1000L;

    private final JwtBuilder jwtBuilder;


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
