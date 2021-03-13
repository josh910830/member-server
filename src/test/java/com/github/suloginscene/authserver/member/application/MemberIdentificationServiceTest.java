package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Email;
import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberPasswordNotMatchedException;
import com.github.suloginscene.authserver.member.domain.Password;
import com.github.suloginscene.authserver.testing.db.RepositoryFacade;
import com.github.suloginscene.authserver.testing.fixture.DefaultMembers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DisplayName("회원 인증 서비스")
class MemberIdentificationServiceTest {

    @Autowired MemberIdentificationService memberIdentificationService;

    @Autowired RepositoryFacade repositoryFacade;

    Member member;
    Email email;
    Password password;


    @BeforeEach
    void setup() {
        member = DefaultMembers.create();
        email = DefaultMembers.EMAIL;
        password = DefaultMembers.RAW_PASSWORD;
    }

    @AfterEach
    void clear() {
        repositoryFacade.clear();
    }


    @Test
    @DisplayName("정상 - 예외 미발생")
    void authenticate_onSuccess_returnsTrue() {
        repositoryFacade.given(member);

        Long id = memberIdentificationService.identify(email, password);

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void authenticate_withNonExistentEmail_throwsException() {
        repositoryFacade.given(member);

        Email nonExistent = new Email("non-existent@email.com");
        Executable action = () -> memberIdentificationService.identify(nonExistent, password);

        assertThrows(UsernameNotFoundException.class, action);
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 예외 발생")
    void authenticate_withWrongPassword_throwsException() {
        repositoryFacade.given(member);

        Password wrong = new Password("wrongPassword");
        Executable action = () -> memberIdentificationService.identify(email, wrong);

        assertThrows(MemberPasswordNotMatchedException.class, action);
    }

}
