package com.github.suloginscene.memberserver.member.api.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@EqualsAndHashCode @ToString
@NoArgsConstructor @AllArgsConstructor
public class MemberPasswordChangeRequest {

    @NotNull
    @Size(min = 8)
    @Getter
    private String newPassword;

}
