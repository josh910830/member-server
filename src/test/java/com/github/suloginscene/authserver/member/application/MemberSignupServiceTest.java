package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MemberSignupServiceTest {

    @Autowired MemberSignupService memberSignupService;


    @Test
    void signup_onSuccess_returnsId() {
        SignupCommand command = new SignupCommand(Emails.VALID, Passwords.VALID);
        Long id = memberSignupService.signup(command);

        assertThat(id).isNotNull();
    }

}
