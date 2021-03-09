package com.github.suloginscene.authserver.jjwthelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import static com.github.suloginscene.authserver.jjwthelper.Base64Utils.encoded;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;


public class JwtFactory {

    private static final long MINUTE = 60 * 1000L;
    private static final int DEFAULT_EXPIRATION_MINUTES = 60;

    private final JwtBuilder jwtBuilder;


    public JwtFactory(String secret) {
        jwtBuilder = Jwts.builder()
                .signWith(HS256, encoded(secret));
    }

    public String of(Long audience) {
        return of(audience, DEFAULT_EXPIRATION_MINUTES);
    }

    public String of(Long audience, int expirationMinutes) {
        Claims claims = Jwts.claims()
                .setAudience(audience.toString());

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMinutes * MINUTE);

        return jwtBuilder
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
    }

}
