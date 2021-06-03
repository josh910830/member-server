package com.github.suloginscene.memberserver.member.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@NoArgsConstructor @AllArgsConstructor
public class MemberVerificationRequest {

    @NotNull(message = "인증토큰을 입력하십시오.")
    @Getter
    private String token;

}
