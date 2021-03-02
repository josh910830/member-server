package com.github.suloginscene.authserver.member.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Password {

    private String password;


    public Password(String password) {
        this.password = password;
    }

}
