package com.github.suloginscene.authserver.jwt.application;

import com.github.suloginscene.authserver.config.JwtProperties;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("JWT 생성 서비스")
class JwtFactoryTest {

    @Autowired JwtFactory jwtFactory;

    @Autowired JwtProperties jwtProperties;

    // TODO register bean
    JwtParser jwtParser;

    String secret;

    Long userId;


    @BeforeEach
    void setup() {
        secret = Base64.getEncoder()
                .encodeToString(jwtProperties.getSecret().getBytes());
        jwtParser = Jwts.parser().setSigningKey(secret);
        userId = 1L;
    }


    @Test
    void create_onSuccess_tokenHasAudience() {
        String jwt = jwtFactory.create(userId);

        assertThat(getAudience(jwt)).isEqualTo(userId.toString());
    }

    private String getAudience(String jwt) {
        return jwtParser.parseClaimsJws(jwt).getBody().getAudience();
    }

}
