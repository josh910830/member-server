package com.github.suloginscene.memberserver.member.domain.temp;

import com.github.suloginscene.exception.RequestException;


class VerificationException extends RequestException {

    VerificationException(Long id) {
        super(id.toString());
    }

}
