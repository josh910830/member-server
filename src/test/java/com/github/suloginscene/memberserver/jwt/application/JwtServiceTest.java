package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.memberserver.member.domain.Member;
import com.github.suloginscene.memberserver.member.domain.Password;
import com.github.suloginscene.memberserver.testing.base.IntegrationTest;
import com.github.suloginscene.memberserver.testing.data.TestingMembers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.suloginscene.memberserver.testing.data.TestingMembers.EMAIL;
import static com.github.suloginscene.memberserver.testing.data.TestingMembers.RAW_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JwtServiceTest extends IntegrationTest {

    @Autowired JwtService jwtService;


    @Test
    @DisplayName("정상 - TokenPair 반환")
    void issue_onSuccess_returnsTokenPair() {
        Member member = TestingMembers.create();
        given(member);

        TokenPair tokenPair = jwtService.issue(EMAIL, RAW_PASSWORD);

        assertThat(tokenPair.getJwt()).isNotNull();
        assertThat(tokenPair.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 - 예외 발생")
    void issue_withNonExistentUsername_throwsException() {
        Executable action = () -> jwtService.issue(EMAIL, RAW_PASSWORD);

        assertThrows(NotFoundException.class, action);
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 예외 발생")
    void issue_withWrongPassword_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Executable action = () -> jwtService.issue(EMAIL, new Password("wrongPass"));

        assertThrows(RequestException.class, action);
    }

}
