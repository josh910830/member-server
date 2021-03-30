package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.MemberRepository;
import com.github.suloginscene.authserver.testing.base.IntegrationTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import com.github.suloginscene.exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.github.suloginscene.authserver.testing.data.TestingMembers.EMAIL;
import static com.github.suloginscene.authserver.testing.data.TestingMembers.RAW_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;


@DisplayName("회원 가입 서비스")
class MemberSignupServiceTest extends IntegrationTest {

    @Autowired MemberSignupService memberSignupService;
    @SpyBean MemberRepository memberRepository;
    @MockBean PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("정상")
    void signup_onSuccess_returnsId() {
        memberSignupService.signup(EMAIL, RAW_PASSWORD);

        then(memberRepository).should().save(any());
    }

    @Test
    @DisplayName("정상 - 패스워드 인코딩")
    void signup_onSuccess_encodePassword() {
        memberSignupService.signup(EMAIL, RAW_PASSWORD);

        then(passwordEncoder).should().encode(anyString());
    }

    @Test
    @DisplayName("이메일 중복 - 예외 발생")
    void signup_onDuplicate_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Executable action = () -> memberSignupService.signup(EMAIL, RAW_PASSWORD);

        assertThrows(RequestException.class, action);
    }

}
