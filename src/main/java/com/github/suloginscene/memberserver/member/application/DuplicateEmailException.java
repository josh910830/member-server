package com.github.suloginscene.memberserver.member.application;

import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.memberserver.member.domain.Email;


class DuplicateEmailException extends RequestException {

    DuplicateEmailException(Email email) {
        super(email.get());
    }

}
