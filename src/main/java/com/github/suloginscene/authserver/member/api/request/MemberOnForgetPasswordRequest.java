package com.github.suloginscene.authserver.member.api.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@EqualsAndHashCode @ToString
@NoArgsConstructor @AllArgsConstructor
public class MemberOnForgetPasswordRequest {

    @NotNull
    @Email
    @Getter
    private String email;

}