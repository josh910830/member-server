package com.github.suloginscene.authserver.member.domain;

import org.springframework.security.core.AuthenticationException;


public class MemberPasswordNotMatchedException extends AuthenticationException {

    public MemberPasswordNotMatchedException(Email email) {
        super(email.get());
    }

}
