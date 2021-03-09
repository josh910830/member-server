package com.github.suloginscene.authserver.testing.fixture;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.Password;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class DefaultMembers {

    public static final String EMAIL_VALUE = "test@email.com";
    public static final Email EMAIL = new Email(EMAIL_VALUE);

    public static final String RAW_PASSWORD_VALUE = "password";
    public static final Password RAW_PASSWORD = new Password(RAW_PASSWORD_VALUE);

    private static final String ENCODED_PASSWORD_VALUE = "{bcrypt}$2a$10$FmaPpqvyiPMG00XhujnEaOK5MBk46lYgIhgmjQ.zsse7E0osgEs4C";
    private static final Password ENCODED_PASSWORD = new Password(ENCODED_PASSWORD_VALUE);


    public static Member create() {
        return new Member(EMAIL, ENCODED_PASSWORD);
    }

}
