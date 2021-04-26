package com.github.suloginscene.memberserver.jwt.api;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class JwtRequest {

    @NotNull(message = "사용자이름을 입력하십시오.")
    private final String username;

    @NotNull(message = "비밀번호를 입력하십시오.")
    private final String password;

}
