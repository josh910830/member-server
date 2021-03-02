package com.github.suloginscene.authserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "client")
@Getter @Setter
public class ClientProperties {

    private String id;
    private String secret;

}
