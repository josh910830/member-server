package com.github.suloginscene.memberserver.member.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NoArgsConstructor @AllArgsConstructor
public class MemberPasswordChangeRequest {

    @NotNull(message = "새 비밀번호를 입력하십시오.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Getter
    private String newPassword;

}
