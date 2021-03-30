package com.github.suloginscene.authserver.member.application;

import com.github.suloginscene.authserver.member.domain.Member;
import com.github.suloginscene.authserver.member.domain.temp.TempMemberRepository;
import com.github.suloginscene.authserver.testing.base.IntegrationTest;
import com.github.suloginscene.authserver.testing.data.TestingMembers;
import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.mail.Mailer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
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

    @SpyBean TempMemberRepository tempMemberRepository;
    //    @SpyBean MemberRepository memberRepository;

    @SpyBean PasswordEncoder passwordEncoder;
    @SpyBean Mailer mailer;


    @Test
    @DisplayName("가입신청 - 인코딩/임시저장/메일")
    void signup_onSuccess_encodesSavesMails() {
        memberSignupService.signup(EMAIL, RAW_PASSWORD);

        then(passwordEncoder).should().encode(anyString());
        then(tempMemberRepository).should().save(any());
        then(mailer).should().send(any());
    }

    @Test
    @DisplayName("가입신청(이메일 중복) - 예외 발생")
    void signup_onDuplicate_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Executable action = () -> memberSignupService.signup(EMAIL, RAW_PASSWORD);

        assertThrows(RequestException.class, action);
    }

}
