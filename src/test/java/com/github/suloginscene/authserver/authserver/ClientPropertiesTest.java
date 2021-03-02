package com.github.suloginscene.authserver.authserver;

import com.github.suloginscene.authserver.config.ClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ClientPropertiesTest {

    @Autowired ClientProperties clientProperties;


    @Test
    void application_onLoad_setProperties() {
        assertThat(clientProperties.getId()).isNotNull();
        assertThat(clientProperties.getSecret()).isNotNull();
    }

}
