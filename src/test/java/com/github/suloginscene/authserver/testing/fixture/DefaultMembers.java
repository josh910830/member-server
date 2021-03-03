package com.github.suloginscene.authserver.testing.fixture;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class DefaultMembers {

    public static final String EMAIL = "test@email.com";

    public static final String RAW_PASSWORD = "password";
    public static final String ENCODED_PASSWORD =
            "{bcrypt}$2a$10$FmaPpqvyiPMG00XhujnEaOK5MBk46lYgIhgmjQ.zsse7E0osgEs4C";


    public static Member create() {
        Email email = new Email(EMAIL);
        Password password = new Password(ENCODED_PASSWORD);
        return new Member(email, password);
    }

}
