package com.github.suloginscene.authserver.member.api;

import lombok.Data;


@Data
public class SignupRequest {

    private final String email;

    private final String password;

}
