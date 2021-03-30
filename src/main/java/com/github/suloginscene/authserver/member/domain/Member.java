package com.github.suloginscene.authserver.member.domain;

import com.github.suloginscene.exception.RequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;


@Entity
@ToString(of = {"id", "email"})
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Email email;

    private Password password;


    public Member(Email email, Password password) {
        this.email = email;
        this.password = password;
    }


    public void checkPassword(Password rawPassword, PasswordEncoder passwordEncoder) {
        boolean matched = password.matches(rawPassword, passwordEncoder);
        if (!matched) {
            throw new RequestException("password not matched for " + this);
        }
    }

    public void changePassword(Password newPassword) {
        password = newPassword;
    }

}
