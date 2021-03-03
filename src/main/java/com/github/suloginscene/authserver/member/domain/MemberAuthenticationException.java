package com.github.suloginscene.authserver.member.domain;

import org.springframework.security.core.AuthenticationException;


public class MemberAuthenticationException extends AuthenticationException {

    public MemberAuthenticationException(Email email) {
        super(email.get());
    }

}
