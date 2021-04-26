package com.github.suloginscene.memberserver.jwt.application;

import com.github.suloginscene.jwt.JwtFactory;
import com.github.suloginscene.memberserver.jwt.domain.RefreshToken;
import com.github.suloginscene.memberserver.member.application.MemberIdentifyingService;
import com.github.suloginscene.memberserver.member.domain.Email;
import com.github.suloginscene.memberserver.member.domain.Password;
import com.github.suloginscene.property.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final MemberIdentifyingService memberIdentifyingService;

    private final JwtFactory jwtFactory;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;

    // TODO clear expired jwt by cron

    public TokenPair issue(Email email, Password password) {
        Long memberId = memberIdentifyingService.identify(email, password);

        String jwt = jwtFactory.create(memberId);

        RefreshToken refreshToken = RefreshToken.of(memberId, expDays());
        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        String refreshTokenValue = saved.getValue();

        return new TokenPair(jwt, refreshTokenValue);
    }


    public TokenPair renew(String refreshTokenValue) {
        RefreshToken refreshToken = findRefreshToken(refreshTokenValue);
        checkExpired(refreshToken);

        refreshToken.extendExpiration(expDays());

        Long memberId = refreshToken.getMemberId();
        String jwt = jwtFactory.create(memberId);

        return new TokenPair(jwt, refreshTokenValue);
    }

    private RefreshToken findRefreshToken(String refreshTokenValue) {
        return refreshTokenRepository.findByValue(refreshTokenValue)
                .orElseThrow(() -> new RefreshTokenException("token not found"));
    }

    private void checkExpired(RefreshToken refreshToken) {
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("expired token of " + refreshToken.getMemberId());
        }
    }


    private int expDays() {
        return securityProperties.getRefreshExpDay();
    }

}
