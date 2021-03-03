package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import org.springframework.dao.DuplicateKeyException;


public class DuplicateEmailException extends DuplicateKeyException {

    public DuplicateEmailException(Email email) {
        super(email.get());
    }

}
