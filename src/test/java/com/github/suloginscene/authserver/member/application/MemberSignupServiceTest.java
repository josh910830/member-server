package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.testing.Emails;
import com.github.suloginscene.authserver.testing.Passwords;
import com.github.suloginscene.authserver.testing.RepositoryProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Long id = memberSignupService.signup(validSignupCommand());

        assertThat(id).isNotNull();
    }

    @Test
    void signup_onSuccess_encodePassword() {
        memberSignupService.signup(validSignupCommand());

        then(passwordEncoder).should().encode(anyString());
    }

    @Test
    void signup_onDuplicate_throwsException() {
        repositoryProxy.given(new Member(Emails.VALID, Passwords.VALID));

        Executable action = () -> memberSignupService.signup(validSignupCommand());

        assertThrows(DuplicateEmailException.class, action);
    }


    private SignupCommand validSignupCommand() {
        return new SignupCommand(Emails.VALID, Passwords.VALID);
    }

}
