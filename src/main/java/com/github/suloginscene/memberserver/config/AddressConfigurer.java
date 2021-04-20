package com.github.suloginscene.memberserver.config;

import com.github.suloginscene.property.AppProperties;
import com.github.suloginscene.string.HrefAssembleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AddressConfigurer implements ApplicationRunner {

    private final AppProperties appProperties;


    @Override
    public void run(ApplicationArguments args) {
        String address = appProperties.getAddress();
        if (address == null) throw new IllegalStateException("app.address is unset");

        HrefAssembleUtil.setAddress(address);
    }

}
