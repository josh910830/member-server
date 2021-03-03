package com.github.suloginscene.authserver.jwt.api;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class JwtRequest {

    @NotNull
    private final String username;

    @NotNull
    private final String password;

}
