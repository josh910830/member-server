package com.github.suloginscene.authserver.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Getter
    private Long id;

    private Email email;

    private Password password;


    public Member(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public void checkPassword(Password rawPassword, PasswordEncoder passwordEncoder) {
        boolean matches = password.matches(rawPassword, passwordEncoder);
        if (!matches) {
            throw new MemberAuthenticationException(email);
        }
    }

}
