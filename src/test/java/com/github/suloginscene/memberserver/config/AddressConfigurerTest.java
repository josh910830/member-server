package com.github.suloginscene.memberserver.config;

import com.github.suloginscene.property.AppProperties;
import com.github.suloginscene.string.HrefAssembleUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.ApplicationArguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


@DisplayName("주소 설정")
class AddressConfigurerTest {

    @Test
    @DisplayName("설정 정상 - 통과")
    void onSet_doesNotThrow() {
        String domain = "https://domain.com";
        AddressConfigurer addressConfigurer = addressConfigurer(domain);

        ApplicationArguments args = mock(ApplicationArguments.class);
        addressConfigurer.run(args);

        String href = HrefAssembleUtil.href("");
        assertThat(href).isEqualTo(domain);
    }

    @Test
    @DisplayName("미설정 - 예외")
    void onUnset_throwsException() {
        AddressConfigurer addressConfigurer = addressConfigurer(null);

        ApplicationArguments args = mock(ApplicationArguments.class);
        Executable action = () -> addressConfigurer.run(args);

        assertThrows(IllegalStateException.class, action);
    }


    private AddressConfigurer addressConfigurer(String domain) {
        AppProperties appProperties = new AppProperties();
        appProperties.setAddress(domain);
        return new AddressConfigurer(appProperties);
    }

}
