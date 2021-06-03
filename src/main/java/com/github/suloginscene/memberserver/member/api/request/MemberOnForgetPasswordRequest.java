package com.github.suloginscene.memberserver.member.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@NoArgsConstructor @AllArgsConstructor
public class MemberOnForgetPasswordRequest {

    @NotNull(message = "사용자이름을 입력하십시오.")
    @Email(message = "사용자이름은 이메일형식에 맞아야 합니다.")
    @Getter
    private String username;

}
