package com.github.suloginscene.memberserver.member.application;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.Password;
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

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;


@DisplayName("회원 수정 서비스")
class MemberConfiguringServiceTest extends IntegrationTest {

    @Autowired MemberConfiguringService memberConfiguringService;

    @SpyBean PasswordEncoder passwordEncoder;
    @SpyBean Mailer mailer;


    @Test
    @DisplayName("비밀번호 변경 - 인코딩/변경")
    void changePassword_onSuccess_encodesUpdates() {
        Member member = TestingMembers.create();
        given(member);

        Long id = member.getId();
        Password newPassword = new Password("newPassword");
        memberConfiguringService.changePassword(id, newPassword);

        then(passwordEncoder).should().encode(any());
        sync(member).checkPassword(newPassword, passwordEncoder);
    }

    @Test
    @DisplayName("비밀번호 분실 처리 - 인코딩/변경/메일")
    void onForgetPassword_onSuccess_encodesUpdatesSends() {
        Member member = TestingMembers.create();
        given(member);

        Email email = member.getEmail();
        memberConfiguringService.onForgetPassword(email);

        then(passwordEncoder).should().encode(any());
        assertThrows(RequestException.class, () -> sync(member).checkPassword(RAW_PASSWORD, passwordEncoder));
        then(mailer).should().send(any());
    }

    @Test
    @DisplayName("비밀번호 분실 처리(회원 없음) - 리소스 없음 예외")
    void onForgetPassword_onNonExistent_throwsException() {
        Email email = new Email("non-existent@email.com");
        Executable action = () -> memberConfiguringService.onForgetPassword(email);

        assertThrows(NotFoundException.class, action);
    }

    @Test
    @DisplayName("탈퇴 - 성공")
    void withdraw_onSuccess_deletes() {
        Member member = TestingMembers.create();
        given(member);

        Long id = member.getId();
        memberConfiguringService.withdraw(id);

        Executable findingAction = () -> sync(member);
        assertThrows(NotFoundException.class, findingAction);
    }

}
