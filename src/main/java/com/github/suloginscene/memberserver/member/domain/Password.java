package com.github.suloginscene.memberserver.member.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class Password {

    private String password;


    public Password(String password) {
        this.password = password;
    }

    public Password encoded(PasswordEncoder passwordEncoder) {
        String encoded = passwordEncoder.encode(password);
        return new Password(encoded);
    }


    boolean matches(Password rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword.password, password);
    }

}
