package com.github.suloginscene.authserver.member.domain;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Password {

    private String password;


    public Password(String password) {
        this.password = password;
    }


    public void encode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

}
