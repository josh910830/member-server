package com.github.suloginscene.memberserver.jwt.application;

import lombok.Data;


@Data
public class TokenPair {

    private final String jwt;
    private final String refreshToken;

}
