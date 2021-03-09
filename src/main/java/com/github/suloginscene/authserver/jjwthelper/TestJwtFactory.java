package com.github.suloginscene.authserver.jjwthelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;


public class TestJwtFactory {

    private static final long MINUTE = 60 * 1000L;
    private static final long HOUR = 60 * MINUTE;

    private final JwtBuilder jwtBuilder = Jwts.builder();
    private final JwtFactory jwtFactory;


    public TestJwtFactory(JwtFactory jwtFactory) {
        this.jwtFactory = jwtFactory;
    }

    public String of(Long userId) {
        return jwtFactory.of(userId);
    }

    public String expired(Long userId) {
        Claims claims = Jwts.claims()
                .setAudience(userId.toString());

        Date now = new Date();
        Date iat = new Date(now.getTime() - HOUR);
        Date exp = new Date(iat.getTime() + MINUTE);

        return jwtBuilder
                .setClaims(claims)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .compact();
    }

    public String invalid(Long userId) {
        String invalidSecret = Base64.getEncoder().encodeToString("invalid".getBytes());
        JwtBuilder invalidBuilder = Jwts.builder().signWith(HS256, invalidSecret);

        Claims claims = Jwts.claims()
                .setAudience(userId.toString());
        Date now = new Date();
        Date exp = new Date(now.getTime() + HOUR);

        return invalidBuilder
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
    }

    public String malformed() {
        return "abc123def456ghi789";
    }

}
