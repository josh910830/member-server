package com.github.suloginscene.authserver.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("local")
public class LocalRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
    }

}
