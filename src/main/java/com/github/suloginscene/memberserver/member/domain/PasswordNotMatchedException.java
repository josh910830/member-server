package com.github.suloginscene.memberserver.member.domain;

import com.github.suloginscene.exception.RequestException;


class PasswordNotMatchedException extends RequestException {

    PasswordNotMatchedException(Long id) {
        super(id.toString());
    }

}
