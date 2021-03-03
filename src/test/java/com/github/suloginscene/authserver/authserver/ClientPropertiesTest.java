package com.github.suloginscene.authserver.authserver;

import com.github.suloginscene.authserver.config.ClientProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("프로퍼티 로딩 (application.properties)")
public class ClientPropertiesTest {

    @Autowired ClientProperties clientProperties;


    @Test
    @DisplayName("정상 - 필드 not null")
    void application_onLoad_fieldsAreNotNull() {
        assertThat(clientProperties.getId()).isNotNull();
        assertThat(clientProperties.getSecret()).isNotNull();
    }

}
