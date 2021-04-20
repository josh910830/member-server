package com.github.suloginscene.memberserver.member.api.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class MemberSignupRequest {

    @NotNull
    @Email
    private final String username;

    @NotNull
    @Size(min = 8)
    private final String password;

}
