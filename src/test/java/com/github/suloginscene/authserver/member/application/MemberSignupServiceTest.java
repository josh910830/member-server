package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import com.github.suloginscene.authserver.testing.RepositoryProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;


@SpringBootTest
class MemberSignupServiceTest {

    @Autowired MemberSignupService memberSignupService;
    @MockBean PasswordEncoder passwordEncoder;

    @Autowired RepositoryProxy repositoryProxy;


    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    void signup_onSuccess_returnsId() {
        SignupCommand command = new SignupCommand(Emails.VALID, Passwords.VALID);
        Long id = memberSignupService.signup(command);

        assertThat(id).isNotNull();
    }

    @Test
    void signup_onSuccess_encodePassword() {
        SignupCommand command = new SignupCommand(Emails.VALID, Passwords.VALID);
        memberSignupService.signup(command);

        then(passwordEncoder).should().encode(anyString());
    }

}
