package com.github.suloginscene.memberserver.member.domain.temp;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.Password;
import com.github.suloginscene.exception.RequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
        verificationToken = VerificationTokenGenerator.generate();
    }


    public void checkVerificationToken(String token) {
        boolean matched = token.equals(verificationToken);
        if (!matched) {
            throw new RequestException("token not matched for " + this);
        }
    }

    public Member toMember() {
        return new Member(email, password);
    }

}
