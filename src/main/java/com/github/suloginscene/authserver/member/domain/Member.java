package com.github.suloginscene.authserver.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private Email email;

    private Password password;


    public Member(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

}
