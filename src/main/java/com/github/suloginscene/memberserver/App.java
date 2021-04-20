package com.github.suloginscene.memberserver;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class App {

    private static final String CONFIG_ROOT = "file:/app/member";
    private static final String CONFIG_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + CONFIG_ROOT + "/application-secret.properties";


    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
                .properties(CONFIG_LOCATIONS)
                .run(args);
    }

}
