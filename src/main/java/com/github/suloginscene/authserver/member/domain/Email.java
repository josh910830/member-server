package com.github.suloginscene.authserver.member.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Email {

    private String email;


    public Email(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return email;
    }

}
