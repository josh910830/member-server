package com.github.suloginscene.memberserver.member.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@EqualsAndHashCode @ToString
@NoArgsConstructor(access = PROTECTED)
public class Email {

    private String email;


    public Email(String email) {
        this.email = email;
    }


    public String get() {
        return email;
    }

}
