package com.github.suloginscene.authserver.jwt.application;

import com.github.suloginscene.authserver.config.JwtProperties;
import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("JWT 생성 서비스")
class JwtFactoryTest {

    @Autowired JwtFactory jwtFactory;

    @Autowired JwtProperties jwtProperties;
    @Autowired JwtParser jwtParser;

    Long userId;


    @BeforeEach
    void setup() {
        userId = 1L;
    }


    @Test
    @DisplayName("정상 - aud 싣은 JWT 발급")
    void create_onSuccess_tokenHasAudience() {
        String jwt = jwtFactory.create(userId);

        assertThat(getAudience(jwt)).isEqualTo(userId.toString());
    }

    private String getAudience(String jwt) {
        return jwtParser.parseClaimsJws(jwt).getBody().getAudience();
    }

}
