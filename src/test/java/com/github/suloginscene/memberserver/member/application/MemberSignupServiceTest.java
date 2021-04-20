package com.github.suloginscene.memberserver.member.application;

import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.MemberRepository;
import com.github.suloginscene.memberserver.member.domain.temp.TempMember;
import com.github.suloginscene.memberserver.member.domain.temp.TempMemberRepository;
import com.github.suloginscene.memberserver.testing.base.IntegrationTest;
import com.github.suloginscene.memberserver.testing.data.TestingMembers;
import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.mail.Mailer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;


@DisplayName("회원 가입 서비스")
class MemberSignupServiceTest extends IntegrationTest {

    @Autowired MemberSignupService memberSignupService;

    @SpyBean TempMemberRepository tempMemberRepository;
    @SpyBean MemberRepository memberRepository;

    @SpyBean PasswordEncoder passwordEncoder;
    @SpyBean Mailer mailer;


    @Test
    @DisplayName("가입신청 - 인코딩/임시저장/메일")
    void signup_onSuccess_encodesSavesSends() {
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


    @Test
    @DisplayName("인증 - 정식 이전")
    void verify_onSuccess_transfers() {
        TempMember tempMember = TestingMembers.temp();
        given(tempMember);

        Long id = tempMember.getId();
        String token = tempMember.getVerificationToken();
        memberSignupService.verify(id, token);

        then(memberRepository).should().save(any());
        then(tempMemberRepository).should().delete(any());
    }

    @Test
    @DisplayName("인증(가입신청 전) - 리소스 없음 예외")
    void verify_beforeSignup_throwsException() {
        Long id = Long.MAX_VALUE;
        String token = "token";
        Executable action = () -> memberSignupService.verify(id, token);

        assertThrows(NotFoundException.class, action);
    }

    @Test
    @DisplayName("인증(잘못된 토큰) - 요청 예외")
    void verify_withInvalidToken_throwsException() {
        TempMember tempMember = TestingMembers.temp();
        given(tempMember);

        Long id = tempMember.getId();
        String token = "INVALID";
        Executable action = () -> memberSignupService.verify(id, token);

        assertThrows(RequestException.class, action);
    }

}
