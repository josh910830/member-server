package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberAuthenticationException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DisplayName("회원 인증 서비스")
class MemberAuthenticationServiceTest {

    @Autowired MemberAuthenticationService memberAuthenticationService;

    @Autowired RepositoryProxy repositoryProxy;

    Member member;
    String email;
    String password;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
    }

    @AfterEach
    void clear() {
        repositoryProxy.clear();
    }


    @Test
    @DisplayName("정상 - 예외 미발생")
    void authenticate_onSuccess_returnsTrue() {
        repositoryProxy.given(member);

        AuthenticationCommand command = new AuthenticationCommand(new Email(email), new Password(password));
        Executable action = () -> memberAuthenticationService.authenticate(command);

        assertDoesNotThrow(action);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void authenticate_withNonExistentEmail_throwsException() {
        repositoryProxy.given(member);

        AuthenticationCommand command = new AuthenticationCommand(new Email("non-existent@email.com"), new Password(password));
        Executable action = () -> memberAuthenticationService.authenticate(command);

        assertThrows(UsernameNotFoundException.class, action);
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 예외 발생")
    void authenticate_withWrongPassword_throwsException() {
        repositoryProxy.given(member);

        AuthenticationCommand command = new AuthenticationCommand(new Email(email), new Password("wrongPassword"));
        Executable action = () -> memberAuthenticationService.authenticate(command);

        assertThrows(MemberAuthenticationException.class, action);
    }

}
