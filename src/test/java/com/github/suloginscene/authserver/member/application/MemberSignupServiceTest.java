package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MemberSignupServiceTest {

    @Autowired MemberSignupService memberSignupService;


    @Test
    void signup_onSuccess_returnsId() {
        Email email = new Email("test@email.com");
        Password password = new Password("password");
        SignupCommand command = new SignupCommand(email, password);
        Long id = memberSignupService.signup(command);

        assertThat(id).isNotNull();
    }

}
