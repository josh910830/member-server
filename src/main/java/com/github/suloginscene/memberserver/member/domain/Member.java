package com.github.suloginscene.memberserver.member.domain;

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
            throw new PasswordNotMatchedException(id);
        }
    }

    public void changePassword(Password newPassword) {
        password = newPassword;
    }

}
