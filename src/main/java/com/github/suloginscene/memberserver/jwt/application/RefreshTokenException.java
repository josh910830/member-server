package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.exception.RequestException;


class RefreshTokenException extends RequestException {

    RefreshTokenException(String message) {
        super(message);
    }

}
