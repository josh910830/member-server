package com.github.suloginscene.memberserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class App {

    private static final String CONFIG_ROOT = "file:/app/member";
    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + CONFIG_ROOT + "/application-db.properties,"
            + CONFIG_ROOT + "/application-jwt.properties,"
            + CONFIG_ROOT + "/application-mail.properties";


    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
