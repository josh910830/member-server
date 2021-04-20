package com.github.suloginscene.memberserver.testing.data;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.Password;
import com.github.suloginscene.memberserver.member.domain.temp.TempMember;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class TestingMembers {

    public static final String EMAIL_VALUE = "test@email.com";
    public static final Email EMAIL = new Email(EMAIL_VALUE);

    public static final String RAW_PASSWORD_VALUE = "password";
    public static final Password RAW_PASSWORD = new Password(RAW_PASSWORD_VALUE);

    private static final String ENCODED_PASSWORD_VALUE = "{bcrypt}$2a$10$FmaPpqvyiPMG00XhujnEaOK5MBk46lYgIhgmjQ.zsse7E0osgEs4C";
    private static final Password ENCODED_PASSWORD = new Password(ENCODED_PASSWORD_VALUE);


    public static TempMember temp() {
        return new TempMember(EMAIL, ENCODED_PASSWORD);
    }

    public static Member create() {
        return new Member(EMAIL, ENCODED_PASSWORD);
    }

}
