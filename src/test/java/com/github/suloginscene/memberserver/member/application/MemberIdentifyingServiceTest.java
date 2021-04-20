package com.github.suloginscene.memberserver.member.application;

import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.Password;
import com.github.suloginscene.memberserver.testing.base.IntegrationTest;
import com.github.suloginscene.memberserver.testing.data.TestingMembers;
import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("회원 인증 서비스")
class MemberIdentifyingServiceTest extends IntegrationTest {

    @Autowired MemberIdentifyingService memberIdentifyingService;


    @Test
    @DisplayName("정상 - 예외 미발생")
    void authenticate_onSuccess_returnsTrue() {
        Member member = TestingMembers.create();
        given(member);

        Long id = memberIdentifyingService.identify(EMAIL, RAW_PASSWORD);

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void authenticate_withNonExistentEmail_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Email nonExistent = new Email("non-existent@email.com");
        Executable action = () -> memberIdentifyingService.identify(nonExistent, RAW_PASSWORD);

        assertThrows(NotFoundException.class, action);
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 예외 발생")
    void authenticate_withWrongPassword_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Password wrong = new Password("wrongPassword");
        Executable action = () -> memberIdentifyingService.identify(EMAIL, wrong);

        assertThrows(RequestException.class, action);
    }

}
