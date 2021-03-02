package com.github.suloginscene.authserver.member.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class SignupRequest {

    @NotNull
    @Email
    private final String email;

    @NotNull
    @Size(min = 8)
    private final String password;

}
