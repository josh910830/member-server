package com.github.suloginscene.authserver.member.domain.temp;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;


@Entity
@ToString(of = {"id", "email"})
@NoArgsConstructor(access = PROTECTED)
public class TempMember {

    @Id @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Email email;

    private Password password;

    @Getter
    private String verificationToken;


    public TempMember(Email email, Password password) {
        this.email = email;
        this.password = password;
        verificationToken = UUID.randomUUID().toString();
    }

}
