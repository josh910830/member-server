package com.github.suloginscene.authserver.member.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class MemberVerificationRequest {

    @NotNull
    private final Long id;

    @NotNull
    private final String token;

}
