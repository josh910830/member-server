package com.github.suloginscene.memberserver.member.api.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class MemberSignupRequest {

    @NotNull(message = "사용자이름을 입력하십시오.")
    @Email(message = "사용자이름은 이메일형식에 맞아야 합니다.")
    private final String username;

    @NotNull(message = "비밀번호를 입력하십시오.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private final String password;

}
