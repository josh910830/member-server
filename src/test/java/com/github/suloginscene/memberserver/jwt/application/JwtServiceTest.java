package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.exception.NotFoundException;
import com.github.suloginscene.exception.RequestException;
import com.github.suloginscene.memberserver.jwt.domain.RefreshToken;
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


@DisplayName("JWT 서비스")
class JwtServiceTest extends IntegrationTest {

    @Autowired JwtService jwtService;
    @Autowired RefreshTokenRepository refreshTokenRepository;


    @Test
    @DisplayName("발급 - TokenPair 반환")
    void issue_onSuccess_returnsTokenPair() {
        Member member = TestingMembers.create();
        given(member);

        TokenPair tokenPair = jwtService.issue(EMAIL, RAW_PASSWORD);

        assertThat(tokenPair.getJwt()).isNotNull();
        assertThat(tokenPair.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("발급(존재하지 않는 사용자) - 예외 발생")
    void issue_withNonExistentUsername_throwsException() {
        Executable action = () -> jwtService.issue(EMAIL, RAW_PASSWORD);

        assertThrows(NotFoundException.class, action);
    }

    @Test
    @DisplayName("발급(잘못된 비밀번호) - 예외 발생")
    void issue_withWrongPassword_throwsException() {
        Member member = TestingMembers.create();
        given(member);

        Executable action = () -> jwtService.issue(EMAIL, new Password("wrongPass"));

        assertThrows(RequestException.class, action);
    }


    @Test
    @DisplayName("갱신 - TokenPair 반환")
    void renew_onSuccess_returnsTokenPair() {
        Long memberId = 1L;

        int expDays = 1;
        RefreshToken refreshToken = RefreshToken.of(memberId, expDays);
        given(refreshToken);

        String refreshTokenValue = refreshToken.getValue();
        TokenPair tokenPair = jwtService.renew(refreshTokenValue);

        assertThat(tokenPair.getJwt()).isNotNull();
        assertThat(tokenPair.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("갱신(존재하지 않는 리프레시토큰) - 예외 발생")
    void renew_withNonExistentRefreshToken_throwsException() {
        Long memberId = 1L;

        int expDays = 1;
        RefreshToken refreshToken = RefreshToken.of(memberId, expDays);

        String refreshTokenValue = refreshToken.getValue();
        Executable action = () -> jwtService.renew(refreshTokenValue);

        assertThrows(RequestException.class, action);
    }

    @Test
    @DisplayName("갱신(만료된 리프레시토큰) - 예외 발생")
    void renew_withExpiredRefreshToken_throwsException() {
        Long memberId = 1L;

        int expDays = 0;
        RefreshToken refreshToken = RefreshToken.of(memberId, expDays);
        given(refreshToken);

        String refreshTokenValue = refreshToken.getValue();
        Executable action = () -> jwtService.renew(refreshTokenValue);

        assertThrows(RequestException.class, action);
    }

    @Test
    @DisplayName("만료 리프레시토큰 삭제")
    void removeExpiredRefreshTokens() {
        RefreshToken exp1 = RefreshToken.of(1L, 0);
        RefreshToken exp2 = RefreshToken.of(2L, 0);
        RefreshToken exp3 = RefreshToken.of(3L, 0);
        RefreshToken valid1 = RefreshToken.of(1L, 1);
        RefreshToken valid2 = RefreshToken.of(2L, 1);
        RefreshToken valid3 = RefreshToken.of(3L, 1);
        given(exp1, exp2, exp3, valid1, valid2, valid3);

        jwtService.removeExpiredRefreshTokens();

        assertThat(refreshTokenRepository.count()).isEqualTo(3);
    }

}
