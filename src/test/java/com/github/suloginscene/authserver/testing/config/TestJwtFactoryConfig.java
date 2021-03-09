package com.github.suloginscene.authserver.testing.config;

import com.github.suloginscene.jjwthelper.JwtFactory;
import com.github.suloginscene.jjwthelper.TestJwtFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
@RequiredArgsConstructor
public class TestJwtFactoryConfig {

    private final JwtFactory jwtFactory;


    @Bean
    TestJwtFactory testJwtFactory() {
        return new TestJwtFactory(jwtFactory);
    }

}
