package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.authserver.testing.db.RepositoryProxy;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("회원가입 서비스")
class MemberSignupServiceTest {

    @Autowired MemberSignupService memberSignupService;
    @MockBean PasswordEncoder passwordEncoder;

    @Autowired RepositoryProxy repositoryProxy;

    SignupCommand command;


    @BeforeEach
    void setup() {
        String email = DefaultMembers.EMAIL;
        String password = DefaultMembers.RAW_PASSWORD;
        command = new SignupCommand(new Email(email), new Password(password));
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    @DisplayName("정상 - Id 반환")
    void signup_onSuccess_returnsId() {
        Long id = memberSignupService.signup(command);

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("정상 - 패스워드 인코딩")
    void signup_onSuccess_encodePassword() {
        memberSignupService.signup(command);

        then(passwordEncoder).should().encode(anyString());
    }

    @Test
    @DisplayName("이메일 중복 - 예외 발생")
    void signup_onDuplicate_throwsException() {
        repositoryProxy.given(DefaultMembers.create());

        Executable action = () -> memberSignupService.signup(command);

        assertThrows(DuplicateEmailException.class, action);
    }

}
