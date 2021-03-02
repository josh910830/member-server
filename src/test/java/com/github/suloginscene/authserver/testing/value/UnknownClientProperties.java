package com.github.suloginscene.authserver.testing.value;

import com.github.suloginscene.authserver.config.ClientProperties;


public class UnknownClientProperties extends ClientProperties {

    public UnknownClientProperties() {
        super.setId("unknown");
        super.setSecret("unknown");
    }

}
